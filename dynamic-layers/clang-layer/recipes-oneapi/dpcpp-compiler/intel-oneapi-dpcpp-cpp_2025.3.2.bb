SUMMARY = "Intel® oneAPI DPC++/C++ Compiler"
DESCRIPTION = "The Intel® oneAPI DPC++/C++ Compiler provides optimizations \
that help your applications run faster on Intel® 64 architectures with support \
for the latest C, C++, and SYCL language standards. This compiler produces \
optimized code that can run significantly faster by taking advantage of the \
ever-increasing core count and vector register width in Intel® Xeon® processors \
and compatible processors."

HOMEPAGE = "https://www.intel.com/content/www/us/en/developer/tools/oneapi/dpc-compiler.html"

LICENSE = "EULA"

inherit oneapi-installer

COMPILERMAINVER = "2025.3"
ONEAPI_COMPONENT_NAME = "dpcpp-compiler"

SRC_URI += "file://vars.sh"

# Use same license as runtime
LIC_FILES_CHKSUM = "file://${WORKDIR}/compiler/${COMPILERMAINVER}/share/doc/compiler/licensing/c/LICENSE;md5=6174ce91a14a1d1a59d5a13a410cb2b4"

RDEPENDS:${PN} += "intel-oneapi-dpcpp-cpp-runtime"
SKIP_FILEDEPS:${PN} = '1'

# doesn't have GNU_HASH (didn't pass LDFLAGS?)
INSANE_SKIP:${PN} += "textrel dev-so dev-elf ldflags already-stripped file-rdeps staticdev rpaths arch useless-rpaths"

fakeroot do_install() {
    # Install compiler files
    install -d ${D}/opt/intel/oneapi/compiler/${COMPILERMAINVER}
    cp -a ${WORKDIR}/compiler/${COMPILERMAINVER}/* ${D}/opt/intel/oneapi/compiler/${COMPILERMAINVER}/
    
    # Install environment setup script
    install -d ${D}/opt/intel/oneapi/compiler/${COMPILERMAINVER}/env
    install -m 0644 ${UNPACKDIR}/vars.sh ${D}/opt/intel/oneapi/compiler/${COMPILERMAINVER}/env/
    
    chown -R root:root ${D}/opt/intel/oneapi/
}

FILES:${PN} += "/opt/intel/oneapi/*"
FILES_SOLIBSDEV = ""

EXCLUDE_FROM_SHLIBS = "1"
BBCLASSEXTEND = "native nativesdk"
