SUMMARY = "oneAPI Level Zero Specification Headers and Loader"
HOMEPAGE = "https://github.com/oneapi-src/level-zero"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=57d73473ddd903607ff1770e146dddfc"

SRC_URI = "git://github.com/oneapi-src/level-zero.git;protocol=https;nobranch=1"
SRCREV = "87a396d3bdff4e6798d3a401224cd5b9b6a634bc"

inherit cmake
DEPENDS += "opencl-headers"

UPSTREAM_CHECK_GITTAGREGEX = "^v(?P<pver>(\d+(\.\d+)+))$"

PACKAGES =+ "${PN}-headers ${PN}-samples ${PN}-loader"

do_install:append () {
        install -d ${D}${bindir} ${D}${libdir}
        install -m 755 ${B}/bin/zello* ${D}${bindir}

        oe_libinstall -C lib libze_null ${D}${libdir}
}


FILES:${PN}-headers = "${includedir}"
FILES:${PN}-samples = "${bindir} ${libdir}/libze_null* ${libdir}/libze_validation*"
FILES:${PN}-loader = "${libdir}"

# PN-loader (non -dev/-dbg/nativesdk- package) contains symlink .so
INSANE_SKIP:${PN}-loader = "dev-so"
INSANE_SKIP:${PN}-samples = "dev-so"
ALLOW_EMPTY:${PN} = "1"
BBCLASSEXTEND = "native nativesdk"
