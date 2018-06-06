ビルド手順
========

ndkソースのコピー
------------
path: app/src/main/jni/ へ commonAPIのコピー後
```
ndk-build
```

*コピーおよびbuildのスクリプト未設定

**追記: jenkinsでのbuildは設定済**

ディレクトリscriptsに詳細記述済

apk作成
------------
build.pyで作成可能(別にpython3じゃなくても大丈夫かも)

project root(build.pyが存在するディレクトリ)にbuild成功後、app-${build_type}.apkが配置されます。

実行例
 
*テスト用*: app-develop.apk
```
python3 build.py develop
```

*ステージンング用*: app-staging.apk
```
python3 build.py staging
```

*本番用*: app-release.apk
```
python3 build.py release
```

注意事項
----------
ndkのバージョンによって抜かれているAPIあり

対応例: atof

1. strtodへの変更
`
(atof -> strtod)
`
2. atof関数の自作

アプリ内金額について
====

ShopFragment#onCreateView()できめうち

| key | value(¥) |
| :--- | :--- |
| gb_jwl_100 | 1,080 |
| gb_jwl_300 | 2,900 |
| gb_jwl_500 | 4,700 |
| gb_jwl_1000 | 8,800 |

フレンド機能QRコードの文字列
==============

## 構成

QRコードに含まれる文字列は

`friendCode + suffix`

## suffix表

suffix: . + 接続先サーバドメイン 

| environment | suffix |
| :--- | :--- |
| test | .com.app-daydelight.dev-prize-exchange |
| staging | .com.top-mission-app.stg-prize-exchange |
| production | .com.top-mission-app.prize-exchange |

## e.g. staging

```
String friendCode = getFriendCode();
QRCode = friendCode + ".com.top-mission-app.stg-prize-exchange";
```

基本Activity, Fragmentの設定方法
=============

## Activity - GBActivityBase継承

1. layoutの設定

GBActivityBase#onCreate()
```
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_common);
    }
```

2. リサイズの設定

| function | description |
| :--- | :--- | 
| GBActivityBase#getFragmentContainerId() | フラグメント配置領域のlayoutID設定 |
| GBActivityBase#getResizeBaseViewId() | リサイズを行う領域のlayoutID設定 |


```
    @Override
    protected int getFragmentContainerId() {
        return R.id.layoutFragment;
    }

    @Override
    protected int getResizeBaseViewId() {
        return R.id.layoutFragment;
    }
```

3. Fragment設定 (e.g. ShowQRFragment)

GBActivityBase#onCreateHandler()

```
    @Override
    protected void onCreateHandler(Bundle bundler) {
        super.onCreateHandler(bundler);
        setFirstFragment(ShowQRFragment.newInstance(getIntent().getStringExtra(FRIEND_CODE_KEY)),
                ShowQRFragment.class.getSimpleName());
    }
```

## Fragment - GBFragmentBase

1. 広告領域の設定

GBFragmentBase#getAdContainerId()
```
    @Override
    protected int getAdContainerId() {
        return R.id.layoutAd;
    }
```

gradleバージョンの更新について
===

gradle version: 3.3

環境によってはバージョンが古いので、ビルドできないかもしれません。
Android Studio 3.0.1で利用している場合、gradle version:4.1以上

