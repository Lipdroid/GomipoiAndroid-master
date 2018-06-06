import subprocess
import shutil
import os
import sys

SETTING_FILE_PATH = "./app/src/main/java/app/define/DebugMode.java"

def change_setting_file(args=[]):
    result = ""
    develop_val = args[0]

    if develop_val == "develop" or develop_val == "staging":
        print("change type: " + develop_val)
    else:
        print("change type: release")

    ignore_maintenance = False

    if len(args) > 1:
        maintenance = args[1]
        if maintenance == 'true':
            ignore_maintenance = True


    with open(SETTING_FILE_PATH) as f:
        for line in f.readlines():
            result += change_mode(line, develop_val, ignore_maintenance)


    write_file = open(SETTING_FILE_PATH, "w")
    write_file.write(result)
    write_file.close()


def change_mode(line="", build_type="release", ignore_maintenance=False):
    new_line = line
    if build_type == "develop":
        if "isTestServer" in line:
            new_line = line.replace("false", "true")
        elif "isStagingServer" in line:
            new_line = line.replace("true", "false")
    elif build_type == "staging":
        if "isTestServer" in line:
            new_line = line.replace("true", "false")
        elif "isStagingServer" in line:
            new_line = line.replace("false", "true")
    else:
        new_line = line.replace("true", "false")


    if "isIgnoreMaintenance" in line:
        if ignore_maintenance:
            new_line = line.replace("false", "true")
        else:
            new_line = line.replace("true", "false")

    return new_line

if __name__ == '__main__':
    change_setting_file(sys.argv[1:])
