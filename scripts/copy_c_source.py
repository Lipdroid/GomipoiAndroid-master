import os
import os.path
import shutil
# from distutils.dir_util import copy_tree

def main():
    home = os.path.dirname(os.path.dirname(__file__))
    jni_path = '../app/src/main/jni/commonAPI'
    src_path = '../../../../commonAPI'
    
    # for root, dirs, files in os.walk(src_path):
    #     for file in files:
    #         print(file)
    shutil.copytree(src_path, jni_path)
    # copy_tree(src_path, jni_path)


if __name__ == '__main__':
    main()
    
