#!/usr/bin/env python3
"""
Extract Intel oneAPI components from offline installer
"""
import sys
import os
import zipfile
import tarfile

def extract_component(installer_dir, component_name, packages, output_tarball):
    """Extract specified packages and create tarball"""
    base_path = os.path.join(installer_dir, 'packages')
    extract_dir = f'/tmp/oneapi-extract-{component_name}'
    
    # Clean up any previous extraction
    if os.path.exists(extract_dir):
        import shutil
        shutil.rmtree(extract_dir)
    
    os.makedirs(extract_dir, exist_ok=True)
    
    print(f"Extracting {component_name} component...")
    
    for pkg in packages:
        cup_file = os.path.join(base_path, pkg, 'cupPayload.cup')
        if os.path.exists(cup_file):
            print(f"  Processing {pkg}...")
            try:
                with zipfile.ZipFile(cup_file, 'r') as zip_ref:
                    zip_ref.extractall(extract_dir)
            except Exception as e:
                print(f"  ERROR: {e}")
                return False
        else:
            print(f"  WARNING: {cup_file} not found")
    
    # Create tarball from _installdir
    installdir = os.path.join(extract_dir, '_installdir')
    if not os.path.exists(installdir):
        print(f"ERROR: {installdir} not found after extraction")
        return False
    
    print(f"Creating tarball {output_tarball}...")
    with tarfile.open(output_tarball, 'w:gz') as tar:
        tar.add(installdir, arcname='.')
    
    # Cleanup
    import shutil
    shutil.rmtree(extract_dir)
    
    print(f"Successfully created {output_tarball}")
    return True

def find_packages(base_path, component_name):
    """Auto-discover package versions by scanning the packages directory"""
    packages_dir = os.path.join(base_path, 'packages')
    
    # Component package patterns to search for
    patterns = {
        'dpcpp-runtime': [
            'intel.oneapi.lin.dpcpp-cpp-common.runtime',
            'intel.oneapi.lin.dpcpp-cpp-common',
            'intel.oneapi.lin.compilers-common.runtime',
        ],
        'dpcpp-compiler': [
            'intel.oneapi.lin.dpcpp-cpp-common',
            'intel.oneapi.lin.compilers-common',
        ],
        'ipp': [
            'intel.oneapi.lin.ipp.devel',
            'intel.oneapi.lin.ipp.runtime',
        ],
        'mkl': [
            'intel.oneapi.lin.mkl.devel',
            'intel.oneapi.lin.mkl.runtime',
        ],
    }
    
    if component_name not in patterns:
        return None
    
    found_packages = []
    for pattern in patterns[component_name]:
        # Find directories matching pattern,v=*
        for entry in os.listdir(packages_dir):
            if entry.startswith(pattern + ',v='):
                found_packages.append(entry)
                break
    
    return found_packages if len(found_packages) == len(patterns[component_name]) else None

if __name__ == '__main__':
    if len(sys.argv) < 4:
        print("Usage: oneapi-extract.py <installer_dir> <component> <output_tarball>")
        sys.exit(1)
    
    installer_dir = sys.argv[1]
    component = sys.argv[2]
    output_tarball = sys.argv[3]
    
    # Auto-discover packages from installer
    packages = find_packages(installer_dir, component)
    
    if not packages:
        print(f"ERROR: Could not find packages for component '{component}'")
        print(f"Available: dpcpp-runtime, dpcpp-compiler, ipp, mkl")
        sys.exit(1)
    
    print(f"Found packages: {packages}")
    success = extract_component(installer_dir, component, packages, output_tarball)
    sys.exit(0 if success else 1)
