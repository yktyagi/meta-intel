SUMMARY = "Crypto Multi-buffer Library"
DESCRIPTION = "Intel® Integrated Performance Primitives (Intel® IPP) Cryptography \
is a secure, fast and lightweight library of building blocks for cryptography, \
highly-optimized for various Intel® CPUs."
HOMEPAGE = "https://github.com/intel/ipp-crypto"

LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://../../../LICENSE;md5=d94a5b4dbbc5c6a0c2ce95ab337df6c4"

SRC_URI = "git://github.com/intel/ipp-crypto;protocol=https;nobranch=1 \
           file://0001-CMakeLists.txt-exclude-host-system-headers.patch;striplevel=4 \
           file://0002-cmake-exclude-Yocto-build-flags.patch;striplevel=4 \
           file://0001-crypto-mb-Make-sure-libs-are-installed-correctly.patch;striplevel=4 \
           "
SRCREV = "afe5ee9a38d57da0ffa37df789ecd1261cbfa2ed"

S = "${UNPACKDIR}/${PN}-${PV}/sources/ippcp/crypto_mb"

DEPENDS = "openssl"

inherit cmake pkgconfig
COMPATIBLE_HOST = '(x86_64).*-linux'

EXTRA_OECMAKE += " -DARCH=intel64"
EXTRA_OECMAKE += " -DTOOLCHAIN_OPTIONS='${TOOLCHAIN_OPTIONS}'"

# Upstream sources in this release trigger a GCC unused-but-set-variable warning
# in SM4 code paths; keep the build strict while avoiding this known false-positive.
TOOLCHAIN_OPTIONS:append = " -Wno-error=unused-but-set-variable"

# v2.2.0 installs the per-CPU private static-library headers under
# ${prefix}/tools/staticlib; ship them in the -dev package.
FILES:${PN}-dev += "${prefix}/tools/staticlib/crypto_mb"
