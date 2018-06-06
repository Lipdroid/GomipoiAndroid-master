
elements = ["        ndk {\n",
'            moduleName "AppModule"\n',
'            cFlags "-std=c99"\n',
'            ldLibs "log"\n',
'            ldLibs "lGLESv2"\n',
'            ldLibs "landroid"\n',
'            ldLibs "lz"\n',
"            abiFilters 'armeabi', 'armeabi-v7a', 'mips', 'x86'\n"+ "        }\n"]

path = 'build.gradle'

elements2 = [
'    externalNativeBuild {',
'        ndkBuild {\n' + \
"            path 'src/main/jni/Android.mk'\n" + \
'        }\n' +\
'    }\n']

res = ''
with open(path) as r:
    for line in r.readlines():
        res += line

for e in elements:
    res = res.replace(e, '')

for e in elements2:
    res = res.replace(e, '')

print(res)

w = open(path, 'w')
w.write(res)
w.close()

