SUMMARY = "Intel(R) METEE Library"
DESCRIPTION = "MEETEE library provides a cross-platform simple \
 programing interface for accessing Intel HECI interfaces on devices \
 found in BigCore and Atom based products."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=2ee41112a44fe7014dce33e26468ba93"

COMPATIBLE_HOST = '(i.86|x86_64).*-linux'

inherit cmake

SRC_URI = "git://github.com/intel/metee.git;branch=master;protocol=https \
           file://0001-Suppress-sign-conversion-warning-only-for-ioctl-call.patch \
"
SRCREV = "2e9390294f47c3d88590cb4a6e0293e31fd4a9db"

# Upstream CMake only installs the C metee.h public header. Consumers such
# as lms include the header-only C++ wrapper <meteepp.h>, so install it
# alongside metee.h.
do_install:append() {
    install -m 0644 ${S}/include/meteepp.h ${D}${includedir}/
}

