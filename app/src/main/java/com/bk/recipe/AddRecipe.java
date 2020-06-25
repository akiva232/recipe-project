package com.bk.recipe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddRecipe extends Fragment implements AdapterView.OnItemSelectedListener {
    EditText recipeName;
    EditText ingredients;
    EditText instructions;
    Spinner category;
    Button save;
    ImageView addImage;
    Uri uri;
    File file;
    private OnFragmentTransaction onFragmentTransaction;
    private static int ID = 0;
    Recipe savedRecipe;
    private static final String RECIPE_TAG = "RECIPE_TAG";
    boolean flage = false;

    public static AddRecipe newInstance(Recipe recipe) {
        AddRecipe addRecipe = new AddRecipe();
        Bundle args = new Bundle();
        args.putParcelable(RECIPE_TAG, recipe);
        addRecipe.setArguments(args);
        addRecipe.flage = true;
        return addRecipe;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (flage) {
            savedRecipe = getArguments().getParcelable(RECIPE_TAG);
            System.out.println(savedRecipe.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recipeName = view.findViewById(R.id.add_name);
        ingredients = view.findViewById(R.id.ingredients);
        instructions = view.findViewById(R.id.instructions);
        category = view.findViewById(R.id.spinner_categories);
        save = view.findViewById(R.id.save);
        addImage = view.findViewById(R.id.img_from_user);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.category_list, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(arrayAdapter);
        category.setOnItemSelectedListener(this);

        if (savedRecipe != null) {
            recipeName.setText(savedRecipe.getName());
            ingredients.setText(savedRecipe.getIngredients());
            instructions.setText(savedRecipe.getInstructions());
            if (savedRecipe.getUri() != null)
                addImage.setImageURI(Uri.parse(savedRecipe.getUri()));
        }

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(getContext());
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe recipe = new Recipe();
                recipe.setName(recipeName.getText().toString());
                recipe.setIngredients(ingredients.getText().toString());
                recipe.setInstructions(instructions.getText().toString());
                recipe.setCategory(category.getSelectedItem().toString());
                if (uri != null)
                    recipe.setUri(uri.toString());


                if (savedRecipe == null) {
                    if (!recipe.getName().equals("")) {
                        RecipeDatabase.getInstance(getContext()).recipeDao().addRecipe(recipe);
                        Toast.makeText(getActivity(), "Recipe added", Toast.LENGTH_LONG).show();
                    }
                } else {
                    recipe.setRecipeID(savedRecipe.getRecipeID());
                    RecipeDatabase.getInstance(getContext()).recipeDao().updateRecipe(recipe);
                    Toast.makeText(getActivity(), "Recipe saved", Toast.LENGTH_LONG).show();
                }
                recipeName.setText("");
                ingredients.setText("");
                instructions.setText("");
                onFragmentTransaction = (OnFragmentTransaction) getContext();
                onFragmentTransaction.beginTransaction(Dest.RECYCLER_VIEW, 2, null);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "The category is: " + text, Toast.LENGTH_SHORT);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        String text = parent.getItemAtPosition(0).toString();
    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"מצלמה", "גלריה", "ביטול"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("מצלמה")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (options[item].equals("גלריה")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                } else if (options[item].equals("ביטול")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        addImage.setImageBitmap(selectedImage);
                        SaveImage(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();

                       // Picasso.get().load(new File(contact.getImageUri())).fit().into(imageView);

                        file = new File(getRealPathFromUri(selectedImage));// need to save the image and permission after exit the app
                        uri = selectedImage;
                        if (selectedImage != null) {

                        }

                    }
                    break;
            }
        }
    }


    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();

        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            // sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
            //     Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
// Tell the media scanner about the new file so that it is
// immediately available to the user.
        MediaScannerConnection.scanFile(getContext(), new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });
    }

    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getContext().getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
