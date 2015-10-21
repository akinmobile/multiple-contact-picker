package com.appsng.multiplecontactselection;

import android.net.Uri;

public class Contact {
	private String name;
	private String phoneNo,contactId,email;
	Uri photo;
	boolean selected;
	
	public String getName() {
		return name;
	}	
	
	public boolean getSelected() {
		return selected;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
	
	public String getContactId() {
		return contactId;
	}
	
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}

	
	public String toString(){
		return name;
	}
	public void setImage(String image_uri) {
		photo = Uri.parse(image_uri);
	}
	
	public Uri getImage(){
		return photo;
	}
	
}
