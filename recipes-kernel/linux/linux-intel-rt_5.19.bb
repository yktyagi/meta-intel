require linux-intel.inc

SRC_URI:prepend = "git://github.com/intel/mainline-tracking.git;protocol=https;name=machine;nobranch=1; \
                    "
# Skip processing of this recipe if it is not explicitly specified as the
# PREFERRED_PROVIDER for virtual/kernel. This avoids errors when trying
# to build multiple virtual/kernel providers, e.g. as dependency of
# core-image-rt-sdk, core-image-rt.
python () {
    if d.getVar("KERNEL_PACKAGE_NAME", True) == "kernel" and d.getVar("PREFERRED_PROVIDER_virtual/kernel") != "linux-intel-rt":
        raise bb.parse.SkipPackage("Set PREFERRED_PROVIDER_virtual/kernel to linux-intel-rt to enable it")
}

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

KMETA_BRANCH = "yocto-5.19"

DEPENDS += "elfutils-native openssl-native util-linux-native"

LINUX_VERSION_EXTENSION ??= "-mainline-tracking-${LINUX_KERNEL_TYPE}"

LINUX_VERSION ?= "5.19.0"
SRCREV_machine ?= "fa899c4db66b32353d91e0e3c48a6eaf72ff5931"
SRCREV_meta ?= "61d7aaaa97297780205a333d529e55136e20cb11"

LINUX_KERNEL_TYPE = "preempt-rt"
UPSTREAM_CHECK_GITTAGREGEX = "^mainline-tracking-v5.19-(?P<pver>rt(\d+)-preempt-rt-(\d+)T(\d+)Z)$"
