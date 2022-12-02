package com.example.woomansi.util;

import com.example.woomansi.R;

public class SetProfileImageUtil {

    private static final int[] profileImages = new int[] {
            R.drawable.ic_profile1,
            R.drawable.ic_profile2,
            R.drawable.ic_profile3,
            R.drawable.ic_profile4,
            R.drawable.ic_profile5,
            R.drawable.ic_profile6
    };

    private static int getProfileId(String profile) {
        int f_result = 0;
        switch (profile){
            case "ic_profile1.png": f_result = 0;
                break;
            case "ic_profile2.png":  f_result= 1;
                break;
            case "ic_profile3.png": f_result = 2;
                break;
            case "ic_profile4.png":  f_result = 3;
                break;
            case "ic_profile5.png":  f_result = 4;
                break;
            case "ic_profile6.png":  f_result = 5;
                break;
        }

        return f_result;
    }

    //프로필 할당하고 싶을 때 이미지뷰의 setImageResource()함수 안에 넣어주면 된다.
    public static int getImageFile(String profile) {
        int profileId = getProfileId(profile);
        return profileImages[profileId];
    }
}
