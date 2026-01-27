# Common class for Intel oneAPI recipes that extract from the offline installer
#
# This class provides common functionality for fetching and extracting
# components from the Intel oneAPI base toolkit offline installer.
#
# Recipes using this class should set:
#   ONEAPI_INSTALLER_VERSION - Installer version (e.g., "2025.3.1")
#   ONEAPI_INSTALLER_BUILD - Build number (e.g., "36")
#   ONEAPI_INSTALLER_HASH - URL hash for download
#   ONEAPI_INSTALLER_SHA256 - SHA256 checksum of installer
#   ONEAPI_COMPONENT_NAME - Component name to extract (e.g., "dpcpp-runtime", "mkl")

ONEAPI_INSTALLER_VERSION ??= "2025.3.1"
ONEAPI_INSTALLER_BUILD ??= "36"
ONEAPI_INSTALLER_HASH ??= "6caa93ca-e10a-4cc5-b210-68f385feea9e"
ONEAPI_INSTALLER_SHA256 ??= "c5757a14fe2dd428528bf6dc0c5a6498c7b135e8cf4ed93635acbf3e64a90850"

SRC_URI = "https://registrationcenter-download.intel.com/akdlm/IRC_NAS/${ONEAPI_INSTALLER_HASH}/intel-oneapi-base-toolkit-${ONEAPI_INSTALLER_VERSION}.${ONEAPI_INSTALLER_BUILD}_offline.sh;name=installer \
           file://oneapi-extract.py"

SRC_URI[installer.sha256sum] = "${ONEAPI_INSTALLER_SHA256}"

S = "${UNPACKDIR}"

COMPATIBLE_HOST = "(x86_64).*-linux"

INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

do_unpack[depends] += "xz-native:do_populate_sysroot"

python do_unpack:append() {
    import subprocess
    import os
    import tarfile
    
    workdir = d.getVar('WORKDIR')
    installer_version = d.getVar('ONEAPI_INSTALLER_VERSION')
    build = d.getVar('ONEAPI_INSTALLER_BUILD')
    dl_dir = d.getVar('DL_DIR')
    component = d.getVar('ONEAPI_COMPONENT_NAME')
    
    if not component:
        bb.fatal('ONEAPI_COMPONENT_NAME must be set in the recipe')
    
    installer_name = f'intel-oneapi-base-toolkit-{installer_version}.{build}_offline.sh'
    installer = f'{dl_dir}/{installer_name}'
    extract_dir = f'{workdir}/intel-oneapi-base-toolkit-{installer_version}.{build}_offline'
    
    # Extract installer
    if not os.path.exists(extract_dir):
        bb.note(f'Extracting Intel oneAPI installer...')
        subprocess.run(['sh', installer, '-x', '--extract-only'], 
                      cwd=workdir, check=True)
    
    # Run extraction script
    extract_script = f'{workdir}/sources/oneapi-extract.py'
    output_tarball = f'{workdir}/{component}.tar.gz'
    
    bb.note(f'Extracting {component} components...')
    subprocess.run(['python3', extract_script, extract_dir, 
                   component, output_tarball], check=True)
    
    # Extract the tarball to workdir
    with tarfile.open(output_tarball, 'r:gz') as tar:
        tar.extractall(workdir)
}

do_compile[noexec] = "1"
