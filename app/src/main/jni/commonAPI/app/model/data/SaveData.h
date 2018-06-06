//
// セーブデータ
//
#ifndef GOMIBAKO_SVN_SAVEDATA_H
#define GOMIBAKO_SVN_SAVEDATA_H

class SaveData
{
// ------------------------------
// Member
// ------------------------------
public:
    int add_point;              // 取得したポイント数
    int put_in_garbage_count;   // ゴミ箱に入ったゴミの数
    int broom_use_count;        // 箒を掃いた回数
    bool broom_broken;          // 箒が壊れたか
    bool garbage_can_broken;    // ゴミ箱が壊れたか

// ------------------------------
// Constructor
// ------------------------------
public:
    SaveData(int add_point, int put_in_garbage_count, int broom_use_count, bool broom_broken, bool garbage_can_broken)
    {
        this->add_point = add_point;
        this->put_in_garbage_count = put_in_garbage_count;
        this->broom_use_count = broom_use_count;
        this->broom_broken = broom_broken;
        this->garbage_can_broken = garbage_can_broken;
    }

// ------------------------------
// Destructor
// ------------------------------
public:
    virtual ~SaveData()
    {
    }

// ------------------------------
// Accesser
// ------------------------------
public:
    SaveData *cloneData()
    {
        return new SaveData(this->add_point, this->put_in_garbage_count, this->broom_use_count, this->broom_broken, this->garbage_can_broken);
    }

};

#endif //GOMIBAKO_SVN_SAVEDATA_H
