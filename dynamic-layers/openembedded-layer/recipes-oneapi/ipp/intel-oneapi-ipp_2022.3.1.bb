DESCRIPTION = "Intel® Integrated Performance Primitives are production-ready \
 building blocks for cross-platform performance. Develop high-performance vision, \
 signal, security, and storage applications with this multithreaded software library."
HOMEPAGE = "https://software.intel.com/content/www/us/en/develop/tools/oneapi/components/ipp.html"

LICENSE = "ISSL"

inherit oneapi-installer

MAXVER = "2022.3"
ONEAPI_COMPONENT_NAME = "ipp"

LIC_FILES_CHKSUM = " \
                     file://${WORKDIR}/ipp/${MAXVER}/share/doc/ipp/licensing/license.txt;md5=d7cdc92ed6c4de1263da879599ddc3e2 \
                     file://${WORKDIR}/ipp/${MAXVER}/share/doc/ipp/licensing/third-party-programs.txt;md5=82d0a7f7eb520a9a914f4f802715be65 \
                     "

COMPATIBLE_HOST:libc-musl = "null"

RDEPENDS:${PN} += "tbb setup-intel-oneapi-env"
INSANE_SKIP:${PN} += "ldflags dev-so already-stripped file-rdeps"

FILES:${PN}-staticdev += "/opt/intel/oneapi/ipp/${MAXVER}/lib/*.a \
                          /opt/intel/oneapi/ipp/${MAXVER}/lib/nonpic/*.a"

fakeroot do_install() {
    # Install IPP files
    install -d ${D}/opt/intel/oneapi/ipp/${MAXVER}
    cp -a ${WORKDIR}/ipp/${MAXVER}/* ${D}/opt/intel/oneapi/ipp/${MAXVER}/
    chown -R root:root ${D}/opt/intel/oneapi/
}

FILES:${PN} += "/opt/intel/oneapi/*"

BBCLASSEXTEND = "native nativesdk"
