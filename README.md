# BatteryInfo
Android 获取电池信息（个人Demo）

实时显示/sys/class/power_supply/battery/ 和 /sys/class/power_supply/bms 和 /sys/class/power_supply/usb 节点的信息，

需要开启超级权限，如果是userdebug版本，请使用以下命令
adb root
adb shell setenforce 0
