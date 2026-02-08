SUMMARY = "VA driver for Intel Gen based graphics hardware"
DESCRIPTION = "Intel Media Driver for VAAPI is a new VA-API (Video Acceleration API) \
user mode driver supporting hardware accelerated decoding, encoding, \
and video post processing for GEN based graphics hardware."

HOMEPAGE = "https://github.com/intel/media-driver"
BUGTRACKER = "https://github.com/intel/media-driver/issues"

LICENSE = "MIT & BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=6aab5363823095ce682b155fef0231f0 \
                    file://media_driver/media_libvpx.LICENSE;md5=d5b04755015be901744a78cc30d390d4 \
                    "

COMPATIBLE_HOST = '(i.86|x86_64).*-linux'

inherit features_check
REQUIRED_DISTRO_FEATURES = "opengl"

DEPENDS += "libva gmmlib"

SRC_URI = "git://github.com/intel/media-driver.git;protocol=https;nobranch=1 \
           file://0003-Force-ARGB-surface-to-tile4-for-ACM.patch \
           file://0004-Fix-failed-4k-videowalll-test-case-and-color-corrupt.patch \
           file://g++-wrapper \
          "

SRCREV = "9e4d199d44a86d409e9922fc37c48ceb88252d35"

COMPATIBLE_HOST:x86-x32 = "null"

UPSTREAM_CHECK_GITTAGREGEX = "^intel-media-(?P<pver>(?!600\..*)\d+(\.\d+)+)$"

inherit cmake pkgconfig

MEDIA_DRIVER_ARCH:x86    = "32"
MEDIA_DRIVER_ARCH:x86-64 = "64"

EXTRA_OECMAKE += " \
                   -DMEDIA_RUN_TEST_SUITE=OFF \
                   -DARCH=${MEDIA_DRIVER_ARCH} \
                   -DMEDIA_BUILD_FATAL_WARNINGS=OFF \
                   -DCMAKE_POLICY_VERSION_MINIMUM=3.5 \
                   -DCMAKE_NINJA_FORCE_RESPONSE_FILE=ON \
                  "

# Workaround for "Argument list too long" error in deep workspace paths.
# GCC spawns cc1plus internally with all command-line arguments, which can exceed
# the kernel's ARG_MAX limit (2MB) when workspace paths are deeply nested.
# The g++ wrapper moves include paths to CPLUS_INCLUDE_PATH environment variable,
# which bypasses ARG_MAX limits entirely.
do_configure:prepend() {
    WRAPPER_DIR="${WORKDIR}/compiler-wrappers"
    mkdir -p "$WRAPPER_DIR"
    
    # Extract just the compiler name from CXX (which contains compiler + flags)
    # e.g., "x86_64-poky-linux-g++ -m64 -march=..." -> "x86_64-poky-linux-g++"
    CXX_COMPILER=$(echo ${CXX} | awk '{print $1}')
    
    # Find the real compiler path
    REAL_CXX_PATH="$(which ${CXX_COMPILER} 2>/dev/null || echo ${CXX_COMPILER})"
    
    WRAPPER_SRC="${WORKDIR}/sources/g++-wrapper"
    [ ! -f "$WRAPPER_SRC" ] && WRAPPER_SRC="${WORKDIR}/g++-wrapper"
    
    if [ -f "$WRAPPER_SRC" ]; then
        sed "s|^REAL_GXX=.*|REAL_GXX=\"$REAL_CXX_PATH\"|" "$WRAPPER_SRC" > "$WRAPPER_DIR/$CXX_COMPILER"
        chmod 0755 "$WRAPPER_DIR/$CXX_COMPILER"
        sed -i "s|set( CMAKE_CXX_COMPILER .* )|set( CMAKE_CXX_COMPILER \"$WRAPPER_DIR/$CXX_COMPILER\" )|g" "${WORKDIR}/toolchain.cmake"
    fi
}

CXXFLAGS:append:x86 = " -D_FILE_OFFSET_BITS=64 -D_LARGEFILE_SOURCE"

do_configure:prepend:toolchain-clang() {
    sed -i -e '/-fno-tree-pre/d' ${S}/media_driver/cmake/linux/media_compile_flags_linux.cmake
}

FILES:${PN} += " \
                 ${libdir}/dri/ \
                 "
