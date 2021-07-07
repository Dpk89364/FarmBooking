package com.example.farmbooking;

public class Upload {

    private String mRentalID;
    private String mName;
    private String mEmail;
    private String mPhone;
    private String mPropertyType;
    private String mPrice;
    private String mAddress;
    private String mpincode;
    private String mlandmark;
    private String mSize;
    private String mImageUrl;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, String email, String phone, String type, String price, String address, String size, String landmark, String pincode, String url, String rentalID) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        if (landmark.trim().equals("")){
            landmark = "No Location";
        }



        mName = name;
        mRentalID = rentalID;
        mEmail = email;
        mPhone = phone;
        mPropertyType = type;
        mPrice = price;
        mAddress = address;
        mSize = size;
        mImageUrl = url;
        mpincode = pincode;
        mlandmark = landmark;
    }



    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getmRentalID() {
        return mRentalID;
    }

    public void setmRentalID(String rental) {
        mRentalID = rental;
    }

    public String getAddress(){
        return mAddress;
    }

    public void setAddress(String address){
        mAddress = address;
    }

    public String getmSize(){
        return mSize;
    }

    public void setmSize(String size){
        mSize = size;
    }

    public String getPropertyType(){
        return mPropertyType;
    }

    public void setPropertyType(String type){
        mPropertyType = type;
    }

    public String getmPrice(){
        return mPrice;
    }

    public void setmPrice(String price){
        mPrice = price;
    }

    public String getEmail(){
        return mEmail;
    }

    public void setEmail(String email){
        mEmail = email;
    }

    public String getPhone(){
        return mPhone;
    }

    public void setPhone(String phone){
        mPhone = phone;
    }

    public String getmpincode() {
        return mpincode;
    }

    public void setmpincode(String pincode) {
        mpincode = pincode;
    }

    public String getmlandmark() {
        return mlandmark;
    }

    public void setmlandmark(String landmark) {
        mlandmark = landmark;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }



}
