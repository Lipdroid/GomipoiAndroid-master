package app.pager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.topmission.gomipoi.R;

import java.util.List;

import app.application.GBApplication;
import app.data.RecipeData;
import app.define.GarbageId;
import app.define.ItemCode;

/**
 */
public class ScrollPagerAdapter extends PagerAdapter {

    // ------------------------------
    // define
    // ------------------------------
    private static final int ITEMS_PER_PAGE = 4;

    // ------------------------------
    // Member
    // ------------------------------
    private Context mContext;
    private List<RecipeData> mList;

    // ------------------------------
    // Constructor
    // ------------------------------
    public ScrollPagerAdapter(Context context, List<RecipeData> recipeList) {
        mContext = context;
        mList = recipeList;

        String currentItemCode = null;
        int currentCount = 0;
        int i = 0;
        while (i < mList.size()) {
            RecipeData data = mList.get(i);

            if (data != null) {
                if (currentItemCode == null || !currentItemCode.equals(data.itemCode)) {
                    if (currentCount % ITEMS_PER_PAGE != 0) {
                        int addCount = ITEMS_PER_PAGE - currentCount % ITEMS_PER_PAGE;

                        for (int j = 0; j < addCount; j++) {
                            mList.add(i, null);
                        }
                        i += addCount;
                    }
                    currentItemCode = data.itemCode;
                    currentCount = 1;
                }
                else currentCount++;
            }
            else currentCount = 0;

            i++;
        }
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if (mList == null || mList.size() == 0) {
            return 0;
        }
        return ((mList.size() - 1) / ITEMS_PER_PAGE) + 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_scroll_pager, container, false);

        RecipeData recipe = null;

        // Data1
        int index = position * ITEMS_PER_PAGE;
        if (index >= 0 && index < mList.size()) {
            recipe = mList.get(index);
        }

        ImageView imageViewTitle = (ImageView) view.findViewById(R.id.imageViewTitle);
        ImageView imageViewCell1 = (ImageView)view.findViewById(R.id.imageViewCell1);
        if (recipe != null) {
            // Title
            if (imageViewTitle != null) {
                imageViewTitle.setImageResource(recipe.getTitleResource());
            }

            // Cell1
            if (imageViewCell1 != null) {
                imageViewCell1.setVisibility(View.VISIBLE);

                ImageView material1 = (ImageView) view.findViewById(R.id.imageViewMaterial11);
                if (material1 != null) {
                    material1.setImageResource(recipe.getMaterialResource(RecipeData.MATERIAL_NO_1));
                }

                ImageView material2 = (ImageView) view.findViewById(R.id.imageViewMaterial12);
                if (material2 != null) {
                    material2.setImageResource(recipe.getMaterialResource(RecipeData.MATERIAL_NO_2));
                }

                ImageView material3 = (ImageView) view.findViewById(R.id.imageViewMaterial13);
                if (material3 != null) {
                    material3.setImageResource(recipe.getMaterialResource(RecipeData.MATERIAL_NO_3));
                }
            }
        } else {
            // Cell1
            if (imageViewCell1 != null) {
                imageViewCell1.setVisibility(View.GONE);
            }
        }

        // Data2
        recipe = null;
        index = position * 4 + 1;
        if (index >= 0 && index < mList.size()) {
            recipe = mList.get(index);
        }

        ImageView imageViewCell2 = (ImageView)view.findViewById(R.id.imageViewCell2);
        if (recipe != null) {
            // Cell2
            if (imageViewCell2 != null) {
                imageViewCell2.setVisibility(View.VISIBLE);

                ImageView material1 = (ImageView) view.findViewById(R.id.imageViewMaterial21);
                if (material1 != null) {
                    material1.setImageResource(recipe.getMaterialResource(RecipeData.MATERIAL_NO_1));
                }

                ImageView material2 = (ImageView) view.findViewById(R.id.imageViewMaterial22);
                if (material2 != null) {
                    material2.setImageResource(recipe.getMaterialResource(RecipeData.MATERIAL_NO_2));
                }

                ImageView material3 = (ImageView) view.findViewById(R.id.imageViewMaterial23);
                if (material3 != null) {
                    material3.setImageResource(recipe.getMaterialResource(RecipeData.MATERIAL_NO_3));
                }            }
        } else {
            // Cell2
            if (imageViewCell2 != null) {
                imageViewCell2.setVisibility(View.GONE);
            }
        }

        // Data3
        recipe = null;
        index = position * 4 + 2;
        if (index >= 0 && index < mList.size()) {
            recipe = mList.get(index);
        }

        ImageView imageViewCell3 = (ImageView)view.findViewById(R.id.imageViewCell3);
        if (recipe != null) {
            // Cell3
            if (imageViewCell3 != null) {
                imageViewCell3.setVisibility(View.VISIBLE);

                ImageView material1 = (ImageView) view.findViewById(R.id.imageViewMaterial31);
                if (material1 != null) {
                    material1.setImageResource(recipe.getMaterialResource(RecipeData.MATERIAL_NO_1));
                }

                ImageView material2 = (ImageView) view.findViewById(R.id.imageViewMaterial32);
                if (material2 != null) {
                    material2.setImageResource(recipe.getMaterialResource(RecipeData.MATERIAL_NO_2));
                }

                ImageView material3 = (ImageView) view.findViewById(R.id.imageViewMaterial33);
                if (material3 != null) {
                    material3.setImageResource(recipe.getMaterialResource(RecipeData.MATERIAL_NO_3));
                }
            }
        } else {
            // Cell3
            if (imageViewCell3 != null) {
                imageViewCell3.setVisibility(View.GONE);
            }
        }

        // Data4
        recipe = null;
        index = position * 4 + 3;
        if (index >= 0 && index < mList.size()) {
            recipe = mList.get(index);
        }

        ImageView imageViewCell4 = (ImageView)view.findViewById(R.id.imageViewCell4);
        if (recipe != null) {
            // Cell4
            if (imageViewCell4 != null) {
                imageViewCell4.setVisibility(View.VISIBLE);

                ImageView material1 = (ImageView) view.findViewById(R.id.imageViewMaterial41);
                if (material1 != null) {
                    material1.setImageResource(recipe.getMaterialResource(RecipeData.MATERIAL_NO_1));
                }

                ImageView material2 = (ImageView) view.findViewById(R.id.imageViewMaterial42);
                if (material2 != null) {
                    material2.setImageResource(recipe.getMaterialResource(RecipeData.MATERIAL_NO_2));
                }

                ImageView material3 = (ImageView) view.findViewById(R.id.imageViewMaterial43);
                if (material3 != null) {
                    material3.setImageResource(recipe.getMaterialResource(RecipeData.MATERIAL_NO_3));
                }
            }
        } else {
            // Cell4
            if (imageViewCell4 != null) {
                imageViewCell4.setVisibility(View.GONE);
            }
        }

        ((GBApplication) mContext).getResizeManager().resize(view);
        container.addView(view);
        return view;
    }

}
