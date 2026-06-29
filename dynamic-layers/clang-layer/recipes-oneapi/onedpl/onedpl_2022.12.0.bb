SUMMARY  = "oneAPI DPC++ Library (oneDPL)"
DESCRIPTION = "The oneAPI DPC++ Library (oneDPL) aims to work with the \
oneAPI DPC++ Compiler to provide high-productivity APIs to developers, \
which can minimize DPC++ programming efforts across devices for high \
performance parallel applications."
HOMEPAGE = "https://github.com/oneapi-src/oneDPL"

LICENSE  = "Apache-2.0-with-LLVM-exception"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2e982d844baa4df1c80de75470e0c5cb \
                    file://third-party-programs.txt;md5=409cd5c825a23043b6bb347861d34b35"

SRC_URI = "git://github.com/uxlfoundation/oneDPL.git;protocol=https;branch=release/2022.12.0 \
            "
SRCREV = "f8a7af46fe73e44cc1f64e8f02e9a25cc9155d1a"

do_compile[noexec] = "1"
do_configure[noexec] = "1"

do_install() {
     install -d -m 755 ${D}${includedir}/onedpl
     cp -r ${S}/include/* ${D}${includedir}/onedpl
}

# Upstream renamed release tags from oneDPL-X.Y.Z-release to
# oneDPL-release-X.Y.Z; match the current scheme.
UPSTREAM_CHECK_GITTAGREGEX = "^oneDPL-release-(?P<pver>\d+(\.\d+)+)$"
