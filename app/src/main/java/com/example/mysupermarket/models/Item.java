package com.example.mysupermarket.models;

public class Item {
    int img,count,position;
    double price,weight;
    String id,imgURL,name,category,by,des,type,sPrice;
    Unit unit,subPack,pack;
    boolean lock;


    public Item() {
    }

    public Item(String name) {
        this.name = name;
    }

    public Item(String name, int img) {
        this.name = name;
        this.img = img;
    }

    public Item(String id,String name,String imgURL,String des) {
        this.id=id;
        this.name=name;
        this.imgURL=imgURL;
        this.des=des;
    }
    public Item(String id,String name,String des,String imgURL,int count,String price,int position) {
        this.position=position;
        this.id=id;
        this.name=name;
        this.count=count;
        this.imgURL=imgURL;
        this.des=des;
        this.sPrice=price;
    }

    public Item(String id,String name,String imgURL,String des,String by,Unit unit,Unit subPack,Unit pack,boolean lock) {
        this.id=id;
        this.name=name;
        this.imgURL=imgURL;
        this.des=des;
        this.by=by;
        this.unit=unit;
        this.subPack=subPack;
        this.pack=pack;
        this.lock=lock;
    }
    public Item(String id,String name,String imgURL,String des,String by,double weight,boolean lock) {
        this.id=id;
        this.name=name;
        this.imgURL=imgURL;
        this.des=des;
        this.by=by;
        this.weight=weight;
        this.lock=lock;
    }



    /*public Item(String category, String name, double price, double weight, String by, boolean lock) {
        this.category=category;
        this.name = name;
        this.price=price;
        this.weight=weight;
        this.by=by;
        this.lock=lock;
    }

    public Item(String category, String name, Unit unit, Unit subPack, Unit pack, String by, boolean lock) {
        this.category=category;
        this.name = name;
        this.unit=unit;
        this.subPack=subPack;
        this.pack=pack;
        this.by=by;
        this.lock=lock;
    }*/


    public int getPosition() {
        return position;
    }

    public int getCount() {
        return count;
    }

    public String getsPrice() {
        return removeZero(Double.parseDouble(sPrice));
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getImgURI() {
        return imgURL;
    }

    public String getCategory() {
        return category;
    }

    public Unit getUnit() {
        return unit;
    }

    public Unit getSubPack() {
        return subPack;
    }

    public Unit getPack() {
        return pack;
    }

    public String getPrice() {
        return removeZero(price);
    }

    public String getWeight() {
        return removeZero(weight);
    }

    public String getDes() {
        return des;
    }

    public String getBy() {
        return by;
    }

    public boolean isLock() {
        return lock;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void setSubPack(Unit subPack) {
        this.subPack = subPack;
    }

    public void setPack(Unit pack) {
        this.pack = pack;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public static class Unit {
        int left;
        double consistNum;
        double Price;
        String Name,consistName;

        public void setConsistNum(double consistNum) {
            this.consistNum = consistNum;
        }

        public void setLeft(int left) {
            this.left = left;
        }

        public void setPrice(double price) {
            Price = price;
        }

        public void setName(String name) {
            Name = name;
        }

        public void setConsistName(String consistName) {
            this.consistName = consistName;
        }

        public String getConsistNum() {
            return removeZero(consistNum);
        }

        public int getLeft() {
            return left;
        }

        public String getPrice() {
            return removeZero(Price);
        }

        public String getName() {
            return Name;
        }

        public String getConsistName() {
            return consistName;
        }


    }
    public static String removeZero(Double price) {
        String[] c = String.valueOf(price).split("\\.");
        if (c[1].equals("0")) {
            return c[0];
        } else {
            return String.valueOf(price);
        }
    }

}
