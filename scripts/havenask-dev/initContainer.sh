##!/bin/bash
groupadd havenask
useradd -l -u 197609 -G havenask -md /home/ -s /bin/bash 
usermod -u 197609 
echo -e "\n ALL=(ALL) NOPASSWD:ALL\n" >> /etc/sudoers
echo "PS1='[\u@havenask \w]\$'" > /etc/profile
echo "export TERM='xterm-256color'" >> /etc/profile
mkdir -p /mnt/ram
mount -t ramfs -o size=20g ramfs /mnt/ram
chmod a+rw /mnt/ram
