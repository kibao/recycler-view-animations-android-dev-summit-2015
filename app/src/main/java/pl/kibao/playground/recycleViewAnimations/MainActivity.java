package pl.kibao.playground.recycleViewAnimations;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mList;
    private ColorsAdapter mAdapter;
    private RadioGroup mOperationsRadioGroup;
    private CheckBox mPredictiveAnimationsCB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOperationsRadioGroup = (RadioGroup) findViewById(R.id.operations);
        mPredictiveAnimationsCB = (CheckBox) findViewById(R.id.predictive_animations);
        ArrayList<Integer> colors = ColorsHelper.generateColors(40);
        mAdapter = new ColorsAdapter(this, colors);
        mAdapter.setItemAction(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mOperationsRadioGroup.getCheckedRadioButtonId()) {
                    case R.id.add_color:
                        addItem(view);
                        break;
                    case R.id.change_color:
                        changeItem(view);
                        break;
                    case R.id.delete_color:
                        deleteItem(view);
                        break;
                }
            }
        });

        mList = (RecyclerView) findViewById(R.id.list);
        mList.setAdapter(mAdapter);
        mList.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return mPredictiveAnimationsCB.isChecked();
            }
        });

        ((CheckBox) findViewById(R.id.custom_animator)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    mList.setItemAnimator(new ColorItemAnimator());
                } else {
                    mList.setItemAnimator(new DefaultItemAnimator());
                }
            }
        });
    }

    private void addItem(View view) {
        int pos = mList.getChildAdapterPosition(view);

        if (pos != RecyclerView.NO_POSITION) {
            int color = ColorsHelper.generateColor();
            mAdapter.addItem(pos, color);

            if (pos == 0) {
                mList.scrollToPosition(0);
            }
        }
    }

    private void changeItem(View view) {
        int pos = mList.getChildAdapterPosition(view);

        if (pos != RecyclerView.NO_POSITION) {
            int color = ColorsHelper.generateColor();
            mAdapter.changeItem(pos, color);
        }
    }

    private void deleteItem(View view) {
        int pos = mList.getChildAdapterPosition(view);

        if (pos != RecyclerView.NO_POSITION) {
            mAdapter.removeItem(pos);
        }
    }
}
