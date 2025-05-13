SUMMARY = "Intel Video Processing Library"
DESCRIPTION = "Intel Video Processing Library (VPL) provides \
a single video processing API for encode, decode, and video processing \
that works across a wide range of accelerators."

HOMEPAGE = "https://github.com/intel/libvpl"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c18ea6bb4786a26bf4eee88a7424a408 \
                    file://third-party-programs.txt;md5=a7af995ed33a1fad14b4ef92e6801994"

SRC_URI = "git://github.com/intel/libvpl.git;protocol=https;branch=main \
           file://0001-vpl.pc.in-dont-pass-pcfiledir-to-cflags.patch \
            "
SRCREV = "c45b5d786bf7cdabbe49ff1bab78693ad78feb78"
S = "${WORKDIR}/git"

inherit cmake
DEPENDS += "libva pkgconfig-native"

PACKAGECONFIG ??= "tools"
PACKAGECONFIG[tools] = "-DBUILD_TOOLS=ON, -DBUILD_TOOLS=OFF, wayland wayland-native wayland-protocols"

EXTRA_OECMAKE = "-DBUILD_EXAMPLES=ON"

do_install:append() {
        mkdir -p ${D}${datadir}/VPL/samples
        install -m 755 ${B}/hello-* ${D}${datadir}/VPL/samples
}

COMPATIBLE_HOST = '(x86_64).*-linux'

PACKAGES =+ "${PN}-examples"

RREPLACES:${PN} = "onevpl"
RCONFLICTS:${PN} = "onevpl"

FILES:${PN}-examples = "${datadir}/vpl \
                       "

FILES_SOLIBSDEV = ""
FILES:${PN}-dev += "${libdir}/libvpl.so"

FILES:${PN} += " ${datadir}/VPL/samples  \
                "
