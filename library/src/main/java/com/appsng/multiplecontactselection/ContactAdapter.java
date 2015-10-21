package com.appsng.multiplecontactselection;

import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ContactAdapter extends BaseAdapter {
    
    private Context context;
    SharedPreferences sharedPref;
	ArrayList<Contact> contacts = new ArrayList<Contact>();
	boolean showCheckBox = false;
    
	public ContactAdapter(Context cont,ArrayList<Contact> contacts,boolean showCheckBox) {
		this.context = cont;
        this.contacts = contacts;
        this.showCheckBox = showCheckBox;
    }

    public int getCount() {
        return contacts.size();
    }
    


    public View getView(final int position, View convertView, ViewGroup parent) {
       
    ViewHolder holder = null;
       if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater  = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

   		 	convertView = inflater.inflate(R.layout.adapter_contacts, null);

                 holder.avatar = (ImageView) convertView.findViewById(R.id.img_contact);
                 holder.phone = (TextView) convertView.findViewById(R.id.phone);
                 holder.name = (TextView) convertView.findViewById(R.id.name);
                 
                 
                 holder.checkbox = (CheckBox) convertView.findViewById(R.id.check);
                 holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                     
                     @Override
                     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                         int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                         contacts.get(getPosition).setSelected(buttonView.isChecked()); // Set the value of checkbox to maintain its state.
                     }
                 });
                 convertView.setTag(holder);
                 convertView.setTag(R.id.check, holder.checkbox);
                 
                 convertView.setTag(holder);     
         }else{
             holder = (ViewHolder) convertView.getTag();
         }
      	holder.checkbox.setTag(position); // This line is important.
       if(showCheckBox){
    	   holder.checkbox.setVisibility(View.VISIBLE);
       }else{
    	   holder.checkbox.setVisibility(View.GONE);
       }

       
        holder.phone.setText(contacts.get(position).getPhoneNo());
        holder.name.setText(contacts.get(position).getName());
        
        
        
        Bitmap photo;
		try {
			photo = retrieveContactPhoto(contacts.get(position).getContactId(), context);
			if(photo != null){
				holder.avatar.setImageBitmap(photo);
			}else{
				holder.avatar.setImageResource(R.mipmap.user);
			}

		} catch (Exception e) {
			holder.avatar.setImageResource(R.mipmap.user);
			e.printStackTrace();
		}
        
		holder.checkbox.setChecked(contacts.get(position).getSelected());
        return convertView;
    }
    
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return super.getItemViewType(position);
	}

    class ViewHolder {
        ImageView avatar;
        TextView phone,name;
        CheckBox checkbox;
    }




    public static Bitmap retrieveContactPhoto(String contactID,Context context) throws IOException {
        Bitmap photo = null;
        InputStream inputStream;
        inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(contactID)));

        if (inputStream != null) {
            photo = BitmapFactory.decodeStream(inputStream);
            return photo;

        }
        return photo;
    }
}