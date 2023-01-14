package com.example.mysupermarket.presenters;


import com.example.mysupermarket.Interfaces.UserView;
import com.example.mysupermarket.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class UserPresenter {

    UserView userView;
    User user;

    public UserPresenter(UserView userView) {
        this.userView = userView;
    }

    void getUserInfo() {

        FirebaseFirestore.getInstance().collection("Users").document(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber())).get()
                .addOnCompleteListener(documentSnapshot -> {
                    user=new User();
                    user.setFullName(Objects.requireNonNull(documentSnapshot.getResult()).get("first name") + " " + documentSnapshot.getResult().get("last name"));
                    user.setEmail((String) documentSnapshot.getResult().get("email"));
                    user.setBirthDate((String) documentSnapshot.getResult().get("birthdate"));

                    userView.onGetUserName(user.getFullName());
                    userView.onGetUserEmail(user.getEmail());
                    userView.onGetUserBirthDate(user.getBirthDate());

                });


    }

    public void getUserName() {
        getUserInfo();
    }

    public void getUserEmail() {
        getUserInfo();
    }

    public void getUserBirthDate() {
        getUserInfo();
    }
}
