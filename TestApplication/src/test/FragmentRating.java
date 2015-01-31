package test;

import com.testapplication.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RatingBar;

public class FragmentRating extends Fragment {
	private int rating;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
  	Bundle savedInstanceState) {
  	  View view = inflater.inflate(R.layout.fragment_rating, container, false);
  	  return view;
    }
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public void fillData(int rating){
		final RatingBar ratingBar = (RatingBar)getView().findViewById(R.id.ratingBar);
		ratingBar.setRating(rating);
		ratingBar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ratingBar.setRating((int)ratingBar.getRating());
			}
		});
	}
}
