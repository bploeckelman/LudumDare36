#!/usr/bin/env bash

convert sprites/Flash_Walk_%d.png[1-3] -fuzz 0% -fill 'rgb(230,211,52)' -opaque 'rgb(48,46,192)' -scene 1 sprites/Flash_Walk_Medium_%d.png
convert sprites/Flash_Punch_%d.png[1-4] -fuzz 0% -fill 'rgb(230,211,52)' -opaque 'rgb(48,46,192)' -scene 1 sprites/Flash_Punch_Medium_%d.png
convert sprites/Flash_Walk_%d.png[1-3] -fuzz 0% -fill 'rgb(255,0,0)' -opaque 'rgb(48,46,192)' -scene 1 sprites/Flash_Walk_Hard_%d.png
convert sprites/Flash_Punch_%d.png[1-4] -fuzz 0% -fill 'rgb(255,0,0)' -opaque 'rgb(48,46,192)' -scene 1 sprites/Flash_Punch_Hard_%d.png

convert sprites/Smart_Phone_Walk_%d.png[0-2] -fuzz 10% -fill 'rgb(230,211,52)' -opaque 'rgb(17,143,226)' sprites/Smart_Phone_Walk_Medium_%d.png
convert sprites/Smart_Phone_Knife_%d.png[0-5] -fuzz 10% -fill 'rgb(230,211,52)' -opaque 'rgb(17,143,226)' sprites/Smart_Phone_Knife_Medium_%d.png
convert sprites/Smart_Phone_Walk_%d.png[0-2] -fuzz 10% -fill 'rgb(255,0,0)' -opaque 'rgb(17,143,226)' sprites/Smart_Phone_Walk_Hard_%d.png
convert sprites/Smart_Phone_Knife_%d.png[0-5] -fuzz 10% -fill 'rgb(255,0,0)' -opaque 'rgb(17,143,226)' sprites/Smart_Phone_Knife_Hard_%d.png
