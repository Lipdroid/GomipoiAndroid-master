import subprocess
import shutil
import os
import sys

SETTING_FILE_PATH = "../app/src/main/java/app/define/DebugMode.java"


def change_setting_file(args=[]):
    result = ""
    develop_val = args[0]

    if develop_val == "develop" or develop_val == "staging":
        print("change type: " + develop_val)
    else:
        print("change type: release")

    
    with open(SETTING_FILE_PATH) as f:
        for line in f.readlines():
            result += change_mode(line, develop_val)


    write_file = open(SETTING_FILE_PATH, "w")
    write_file.write(result)
    write_file.close()


def change_mode(line="", build_type="release"):
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

    return new_line


def build_cmd():
    cmd = "../gradlew clean assembleRelease -Pandroid.injected.signing.store.file=topmission_release.jks -Pandroid.injected.signing.store.password=20160706 -Pandroid.injected.signing.key.alias=topmission_release -Pandroid.injected.signing.key.password=20160706"

    subprocess.call(cmd, shell=True)


if __name__ == '__main__':
    
    change_setting_file(sys.argv[1:])

    build_name = sys.argv[1]
    build_cmd()
    # move
    apk_path = "app/build/outputs/apk/app-release.apk"
    shutil.move(apk_path, "./app-" + build_name +  ".apk")
    
