package com.teamtreehouse.colorizer;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

//FUNCTIONALITY: Heavy use of Options menus. [Resources-->Right-Click-->New Resource File Direcotry-->name Menu-->Menu-RichtClick--> New REsource File-->options.xml
          // Cycle through multiple photos with the Next photo icon.
            //Allow each photo to be edited to remove all red, blue or yellow from the photo
            //Offer a reset button and allow multiple colors to be removed simultaneously.
            //Allow user to choose if the image is BW or Color. If BW, the color editing options will disappear.


//NOTE: Imported Glide to handle Android "Out of Memory" Errors when a large photo is uploaded


public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    int[] imageResIds = {R.drawable.cuba1, R.drawable.cuba2, R.drawable.cuba3};
    int imageIndex = 0;
    boolean color = true;
    boolean red = true;
    boolean green = true;
    boolean blue = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.imageView);
        loadImage();
    }

    private void loadImage() {
        Glide.with(this).load(imageResIds[imageIndex]).into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Add Items to menu by hardcoding or by using menu resource.

        /*   HARDCODING OPTIONS MENU LOGIC
        MenuItem menuItem = menu.add ("Next Image");

        //display menu (Will display in overflow (...) menu instead of the action bar unless the action bar is specified.
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        //NOTE: ADDING an icon to this menu item using just android studio. Right-Click-drawable-folder --> New Vector Asset--> Icon Button--> search photo
            menuItem.setIcon(R.drawable.ic_add_a_photo_black_24dp);
            //chose to edit color of icon programmatically so that it can easily be changed at runtime. We could have also simply changed the hex code in the drawable file in Resources
            menuItem.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP); //Every non-transparent pixel will be turned to white
                return true;
        */

        //manually go into resources menu folder and use the design tab or xml to add each icon, make the colors checkable, etc
        getMenuInflater().inflate(R.menu.options_menu, menu);
        //get drawable icon and change the color
        Drawable nextImageDrawable = menu.findItem(R.id.nextImage).getIcon();
        nextImageDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        //make checkboxes in menu item work
        menu.findItem(R.id.red).setChecked(red);
        menu.findItem(R.id.green).setChecked(green);
        menu.findItem(R.id.blue).setChecked(blue);

        menu.setGroupVisible(R.id.colorGroup, color); //NOTE: setGroupVisible(insert group, boolean)...this makes the visibility of R.id.colorGroup True if color is true and false if color is false.
        //The purpose of this is to remove the color options if the image is black and white

        return true;

    }

    @Override
    //called whenever a menu item is called.
    public boolean onOptionsItemSelected(MenuItem item) {

        //Will set to react differently depending on button selected. Use switch for each menu item
        switch (item.getItemId()){
//next Image Display button
            case R.id.nextImage:
                imageIndex++;
                        if (imageIndex >= imageResIds.length)  //if the index goes over the capacity, it will start over with the first image
                        {imageIndex = 0;
                        }
                            loadImage();
                            break;

//Color or BW button
            case R.id.color: //Is it color or BW
               //Must update the color variable. Tell program that the current color is not the right one and then update Saturation
                color = !color;
                updateSaturation();
                //Hide Red, Green & Blue Options. I have grouped the red, green and blue options in the xml.
                invalidateOptionsMenu(); //This invalidates the options, and ends with a call to OnCreate. For this reason, OnCreate logic must be updated to adjust how this functions.
                                            //Again, this method is managed using OnCreate(). The colors above have been assigned to a group, but this group visibility must be set in the OnCreateMethod.
                break;
//individual color options
            case R.id.red:
                    red = !red;
                    updateColors();

                    item.setChecked(red); //When this is clicked, set the checkbox to checked status
                break;

            case R.id.green:
                green = !green;
                updateColors();

                item.setChecked(green);
                break;

            case R.id.blue:
                blue = !blue;
                updateColors();

                item.setChecked(blue);
                break;

//Reset Colors Option
            case R.id.reset:
                imageView.clearColorFilter();

                red=green=blue=color=true; //set the colors button equal to true again

                invalidateOptionsMenu(); //set the options menu to the way it once was

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void updateSaturation() {
        ColorMatrix colorMatrix = new ColorMatrix();
        if (color) {
            red = green = blue = true;
            colorMatrix.setSaturation(1);
        } else {
            colorMatrix.setSaturation(0);
        }
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        imageView.setColorFilter(colorFilter);
    }

    private void updateColors() {
        ColorMatrix colorMatrix = new ColorMatrix();
        float[] matrix = {
                1, 0, 0, 0, 0,
                0, 1, 0, 0, 0,
                0, 0, 1, 0, 0,
                0, 0, 0, 1, 0,
        };
        if (!red) matrix[0] = 0;
        if (!green) matrix[6] = 0;
        if (!blue) matrix[12] = 0;
        colorMatrix.set(matrix);
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        imageView.setColorFilter(colorFilter);
    }
}
