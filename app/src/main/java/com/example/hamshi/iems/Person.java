
package com.example.hamshi.iems;


/**
 * Created by hamshi on 07-06-2016.
 */

class Person {
    private int id;
    private int phone;
    private String name;
    private String addres;
    private String occupation;
    private int rate;

    public Person(){
        super();
    }

    public Person(int id, int phone, String name, String addres, String occupation, int rate) {
        super();
        this.id = id;
        this.phone=phone;
        this.name = name;
        this.addres=addres;
        this.occupation = occupation;
        this.rate = rate;

    }
    public int getId()
    {
        return id;
    }
    public int getPhone()
    {
        return phone;
    }
    public String getName()
    {
        return name;
    }
    public String getAddres()
    {
        return addres;
    }
    public String getoccupation()
    {
        return occupation;
    }
    public int getRate()
    {
        return rate;
    }

    @Override
    public String toString() {

        return this.id + "     " +  this.phone +  "  " +  this.addres + "       " + this.name + "                [$" + this.rate + "]";

    }
}
