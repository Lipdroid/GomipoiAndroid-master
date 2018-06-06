import sys

def main():
    args = sys.argv[1:]
    gradle_path = args[0]

    version_code_element = None
    version_name_element = None

    version_code_key = 'versionCode'
    version_name_key = 'versionName'

    for arg in args:
        if version_code_key in arg:
            version_code_element = arg
        elif version_name_key in arg:
            version_name_element = arg


    version_code = version_code_element.split('=')[1]
    version_name = version_name_element.split('=')[1]

    import re
    r_code = re.compile('\d{1,100}')
    r_name = re.compile('\d{1,100}.\d{1,100}.\d{1,100}')

    strings = ""

    with open(gradle_path, 'r') as r:
        for line in r.readlines():
            new_line = line
            if version_code_key in line:
                new_line = r_code.sub(version_code, line)
            elif version_name_key in line:
                new_line = r_name.sub(version_name, line)

            strings += new_line

    w = open(gradle_path, 'w')
    w.write(strings)
    w.close()


if __name__ == '__main__':
    main()
    