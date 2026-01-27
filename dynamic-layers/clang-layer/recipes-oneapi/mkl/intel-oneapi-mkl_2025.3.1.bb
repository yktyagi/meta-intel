SUMMARY = "Intel® oneAPI Math Kernel Library (oneMKL)"
DESCRIPTION = "The Intel® oneAPI Math Kernel Library (oneMKL) is a computing \
 math library of highly optimized and extensively parallelized routines \
 for applications that require maximum performance. oneMKL contains \
 the high-performance optimizations from the full Intel® Math Kernel Library \
 for CPU architectures (with C/Fortran programming language interfaces)\
 and adds to them a set of DPC++ programming language interfaces for \
 achieving performance on various CPU architectures \
 and Intel Graphics Technology for certain key functionalities."
HOMEPAGE = "https://software.intel.com/content/www/us/en/develop/tools/oneapi/components/onemkl.html"

LICENSE = "ISSL"


inherit oneapi-installer

MKLMAINVER = "2025.3"
ONEAPI_COMPONENT_NAME = "mkl"

LIC_FILES_CHKSUM = " \
                     file://${WORKDIR}/mkl/${MKLMAINVER}/share/doc/mkl/licensing/license.txt;md5=c1f422b8b1f08b19ac1e17a6b7cbe5a3  \
                     file://${WORKDIR}/mkl/${MKLMAINVER}/share/doc/mkl/licensing/third-party-programs-benchmarks.txt;md5=cb98e1a1f14c05ea85a979ea8982e7a4 \
                     file://${WORKDIR}/mkl/${MKLMAINVER}/share/doc/mkl/licensing/third-party-programs.txt;md5=2f25a289a88443cdb21b39304ad0652d \
                     "

RDEPENDS:${PN} += "bash tbb intel-oneapi-dpcpp-cpp-runtime setup-intel-oneapi-env virtual-opencl-icd"
INSANE_SKIP:${PN} = "ldflags textrel dev-so staticdev arch already-stripped file-rdeps"

fakeroot do_install() {
    # Install MKL files
    install -d ${D}/opt/intel/oneapi/mkl/${MKLMAINVER}
    cp -a ${WORKDIR}/mkl/${MKLMAINVER}/* ${D}/opt/intel/oneapi/mkl/${MKLMAINVER}/
    chown -R root:root ${D}/opt/intel/oneapi/

    # Create convenience symlinks
    install -d ${D}${bindir}
    (cd ${D}${bindir} ; ln -s ../../opt/intel/oneapi/mkl/${MKLMAINVER}/bin/* .)
    
    install -d ${D}${libdir}
    (cd ${D}${libdir} ; ln -s ../../opt/intel/oneapi/mkl/${MKLMAINVER}/lib/*.so* .)
    (cd ${D}${libdir} ; ln -s ../../opt/intel/oneapi/mkl/${MKLMAINVER}/lib/*.a* .)
    
    install -d ${D}${libdir}/pkgconfig
    (cd ${D}${libdir}/pkgconfig ; ln -s ../../../opt/intel/oneapi/mkl/${MKLMAINVER}/lib/pkgconfig/* .)
    
    install -d ${D}${libdir}/cmake
    (cd ${D}${libdir}/cmake ; ln -s ../../../opt/intel/oneapi/mkl/${MKLMAINVER}/lib/cmake/* .)

    install -d ${D}${includedir}
    find ${D}/opt/intel/oneapi/mkl/${MKLMAINVER}/include/ -mindepth 1 -maxdepth 1 -type d -printf '%f\n' | while read srcdir; do
        install -d ${D}${includedir}/$srcdir
        (cd ${D}${includedir} ; ln -s ../../opt/intel/oneapi/mkl/${MKLMAINVER}/include/$srcdir/* ./$srcdir/ 2>/dev/null || true)
    done

    find ${D}/opt/intel/oneapi/mkl/${MKLMAINVER}/include/ -mindepth 1 -maxdepth 1 -type f -printf '%f\n' | while read srcfile; do
        (cd ${D}${includedir} ; ln -s ../../opt/intel/oneapi/mkl/${MKLMAINVER}/include/$srcfile . 2>/dev/null || true)
    done
}

AUTO_LIBNAME_PKGS = ""
FILES:${PN} += "/opt/intel/oneapi/*"
FILES:${PN}-staticdev += "/opt/intel/oneapi/mkl/${MKLMAINVER}/lib/*.a*"

SKIP_FILEDEPS:${PN} = '1'
SYSROOT_DIRS += "/opt"

BBCLASSEXTEND = "native nativesdk"
