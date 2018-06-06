import sys

setting_file_path = './scripts/app/src/main/jni/commonAPI/app/manager/GameManager.h'

change_value = '#define SP_APPEAR_TEST_FLAG '

def main(debug=False):
    result = ''
    with open(setting_file_path) as r:
        for line in r.readlines():
            if change_value in line:
                if debug:
                    new_line = line.replace(change_value + '0', change_value + '1')
                else:
                    new_line = line.replace(change_value + '1', change_value + '0')
                result += new_line
            else:
                result += line

    w = open(setting_file_path, 'w')
    w.write(result)
    w.close()


if __name__ == '__main__':
    flag = sys.argv[1]
    is_debug = False
    if flag == 'true':
        is_debug = True

    main(is_debug)
