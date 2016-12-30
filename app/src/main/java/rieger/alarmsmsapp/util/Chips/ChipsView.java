package rieger.alarmsmsapp.util.Chips;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rieger.alarmsmsapp.R;

/**
 * Created by sebastian on 30.12.16.
 */

public class ChipsView extends ScrollView implements ChipsEditText.InputConnectionWrapperInterface{
    //<editor-fold desc="Static Fields">
    private static final String LOG_TAG = "ChipsView";
    private static final int CHIP_HEIGHT = 32; // dp
    private static final int SPACING_TOP = 4; // dp
    private static final int SPACING_BOTTOM = 4; // dp
    public static final int DEFAULT_VERTICAL_SPACING = 1; // dp
    private static final int DEFAULT_MAX_HEIGHT = -1;
    //</editor-fold>

    //<editor-fold desc="Resources">
    private int mChipsBgRes = R.drawable.chip_background;
    //</editor-fold>

    //<editor-fold desc="Attributes">
    private int mMaxHeight; // px
    private int mVerticalSpacing;

    private int mChipsColor;
    private int mChipsColorClicked;
    private int mChipsColorErrorClicked;
    private int mChipsBgColor;
    private int mChipsBgColorClicked;
    private int mChipsBgColorErrorClicked;
    private int mChipsTextColor;
    private int mChipsTextColorClicked;
    private int mChipsTextColorErrorClicked;
    private int mChipsPlaceholderResId;
    private int mChipsDeleteResId;

    private String mChipsHint;
    //</editor-fold>

    //<editor-fold desc="Private Fields">
    private float mDensity;
    private RelativeLayout mChipsContainer;
    private ChipsListener mChipsListener;
    private ChipsEditText mEditText;
    private ChipsVerticalLinearLayout mRootChipsLayout;
    private EditTextListener mEditTextListener;
    private List<Chip> mChipList = new ArrayList<>();
    private Object mCurrentEditTextSpan;
    private ChipValidator mChipsValidator;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public ChipsView(Context context) {
        super(context);
        init();
    }

    public ChipsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }

    public ChipsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChipsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(context, attrs);
        init();
    }
    //</editor-fold>

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mMaxHeight != DEFAULT_MAX_HEIGHT) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(mMaxHeight, View.MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        return true;
    }

    //<editor-fold desc="Initialization">
    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ChipsView,
                0, 0);
        try {
            mMaxHeight = a.getDimensionPixelSize(R.styleable.ChipsView_cv_max_height, DEFAULT_MAX_HEIGHT);
            mVerticalSpacing = a.getDimensionPixelSize(R.styleable.ChipsView_cv_vertical_spacing, (int) (DEFAULT_VERTICAL_SPACING * mDensity));
            mChipsColor = a.getColor(R.styleable.ChipsView_cv_color,
                    ContextCompat.getColor(context, R.color.base30));
            mChipsColorClicked = a.getColor(R.styleable.ChipsView_cv_color_clicked,
                    ContextCompat.getColor(context, R.color.colorPrimaryDark));
            mChipsColorErrorClicked = a.getColor(R.styleable.ChipsView_cv_color_error_clicked,
                    ContextCompat.getColor(context, R.color.color_error));

            mChipsBgColor = a.getColor(R.styleable.ChipsView_cv_bg_color,
                    ContextCompat.getColor(context, R.color.base10));
            mChipsBgColorClicked = a.getColor(R.styleable.ChipsView_cv_bg_color_clicked,
                    ContextCompat.getColor(context, R.color.blue));

            mChipsBgColorErrorClicked = a.getColor(R.styleable.ChipsView_cv_bg_color_clicked,
                    ContextCompat.getColor(context, R.color.color_error));

            mChipsTextColor = a.getColor(R.styleable.ChipsView_cv_text_color,
                    Color.BLACK);
            mChipsTextColorClicked = a.getColor(R.styleable.ChipsView_cv_text_color_clicked,
                    Color.WHITE);
            mChipsTextColorErrorClicked = a.getColor(R.styleable.ChipsView_cv_text_color_clicked,
                    Color.WHITE);

            mChipsPlaceholderResId = a.getResourceId(R.styleable.ChipsView_cv_icon_placeholder,
                    R.drawable.ic_person_24dp);
            mChipsDeleteResId = a.getResourceId(R.styleable.ChipsView_cv_icon_delete,
                    R.drawable.ic_close_24dp);

            mChipsHint = a.getString(R.styleable.ChipsView_cv_hint);

        } finally {
            a.recycle();
        }
    }

    private void init() {
        mDensity = getResources().getDisplayMetrics().density;

        mChipsContainer = new RelativeLayout(getContext());
        addView(mChipsContainer);

        // Dummy item to prevent AutoCompleteTextView from receiving focus
        LinearLayout linearLayout = new LinearLayout(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(0, 0);
        linearLayout.setLayoutParams(params);
        linearLayout.setFocusable(true);
        linearLayout.setFocusableInTouchMode(true);

        mChipsContainer.addView(linearLayout);

        mEditText = new ChipsEditText(getContext(), this);
        mEditText.setHint(mChipsHint);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = (int) (SPACING_TOP * mDensity);
        layoutParams.bottomMargin = (int) (SPACING_BOTTOM * mDensity) + mVerticalSpacing;
        mEditText.setLayoutParams(layoutParams);
        mEditText.setMinHeight((int) (CHIP_HEIGHT * mDensity * 1.5));
        mEditText.setPadding(0, 0, 0, 0);
        mEditText.setLineSpacing(mVerticalSpacing, (CHIP_HEIGHT * mDensity) / mEditText.getLineHeight());
        mEditText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_ACTION_UNSPECIFIED);
        mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        mChipsContainer.addView(mEditText);

        mRootChipsLayout = new ChipsVerticalLinearLayout(getContext(), mVerticalSpacing);
        mRootChipsLayout.setOrientation(LinearLayout.VERTICAL);
        mRootChipsLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mRootChipsLayout.setPadding(0, (int) (SPACING_TOP * mDensity), 0, 0);
        mChipsContainer.addView(mRootChipsLayout);

        initListener();
    }

    private void initListener() {
        mChipsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.requestFocus();
                unselectAllChips();
            }
        });

        mEditTextListener = new EditTextListener();
        mEditText.addTextChangedListener(mEditTextListener);
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    unselectAllChips();
                }
            }
        });
    }
    //</editor-fold>

    //<editor-fold desc="Public Methods">
    public void addChip(String displayName, String avatarUrl, Content content) {
        addChip(displayName, Uri.parse(avatarUrl), content);
    }

    public void addChip(String displayName, Uri avatarUrl, Content content) {
        addChip(displayName, avatarUrl, content, false);
        mEditText.setText("");
        addLeadingMarginSpan();
    }

    public void addChip(String displayName, Uri avatarUrl, Content content, boolean isIndelible) {
        Chip chip = new Chip(displayName, avatarUrl, content, isIndelible);
        mChipList.add(chip);
        if (mChipsListener != null) {
            mChipsListener.onChipAdded(chip);
        }

        onChipsChanged(true);
        post(new Runnable() {
            @Override
            public void run() {
                fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    @NonNull
    public List<Chip> getChips() {
        return Collections.unmodifiableList(mChipList);
    }

    public boolean removeChipBy(Content content) {
        for (int i = 0; i < mChipList.size(); i++) {
            if (mChipList.get(i).mContent != null && mChipList.get(i).mContent.equals(content)) {
                mChipList.remove(i);


                onChipsChanged(true);
                return true;
            }
        }
        return false;
    }

    public void removeChipByIndex(int index){
        mChipList.remove(index);
        onChipsChanged(true);
    }

    public void setChipsListener(ChipsListener chipsListener) {
        this.mChipsListener = chipsListener;
    }

    public void setChipsValidator(ChipValidator chipsValidator) {
        mChipsValidator = chipsValidator;
    }

    public EditText getEditText() {
        return mEditText;
    }
    //</editor-fold>

    //<editor-fold desc="Private Methods">
    /**
     * rebuild all chips and place them right
     */
    private void onChipsChanged(final boolean moveCursor) {
        ChipsVerticalLinearLayout.TextLineParams textLineParams = mRootChipsLayout.onChipsChanged(mChipList);

        // if null then run another layout pass
        if (textLineParams == null) {
            post(new Runnable() {
                @Override
                public void run() {
                    onChipsChanged(moveCursor);
                }
            });
            return;
        }

        if(mChipList.size() == 0){
            mEditText.setHint(mChipsHint);
        }else {
            mEditText.setHint("");
        }



        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mEditText.getLayoutParams();
        params.topMargin = (int) ((SPACING_TOP + textLineParams.row * CHIP_HEIGHT) * mDensity) + textLineParams.row * mVerticalSpacing;
        mEditText.setLayoutParams(params);
        addLeadingMarginSpan(textLineParams.lineMargin);
        if (moveCursor) {
            mEditText.setSelection(mEditText.length());
        }
    }

    private void addLeadingMarginSpan(int margin) {
        Spannable spannable = mEditText.getText();
        if (mCurrentEditTextSpan != null) {
            spannable.removeSpan(mCurrentEditTextSpan);
        }
        mCurrentEditTextSpan = new android.text.style.LeadingMarginSpan.LeadingMarginSpan2.Standard(margin, 0);
        spannable.setSpan(mCurrentEditTextSpan, 0, 0, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        mEditText.setText(spannable);
    }

    private void addLeadingMarginSpan() {
        Spannable spannable = mEditText.getText();
        if (mCurrentEditTextSpan != null) {
            spannable.removeSpan(mCurrentEditTextSpan);
        }
        spannable.setSpan(mCurrentEditTextSpan, 0, 0, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        mEditText.setText(spannable);
    }


    private void onEnterPressed(String text) {
        Chip chip = new Chip(text, null, new Content(null, text, null));
        mChipList.add(chip);
        if (mChipsListener != null) {
            mChipsListener.onChipAdded(chip);
        }
        post(new Runnable() {
            @Override
            public void run() {
                onChipsChanged(true);
            }
        });
    }

    public void readyToSave(){
        if(!mEditText.getText().toString().isEmpty()) {
            onEnterPressed(mEditText.getText().toString());
        }
        mEditText.setText("");
    }

    private void selectOrDeleteLastChip() {
        if (mChipList.size() > 0) {
            onChipInteraction(mChipList.size() - 1);
        }
    }

    private void onChipInteraction(int position) {
        try {
            Chip chip = mChipList.get(position);
            if (chip != null) {
                onChipInteraction(chip, true);
            }
        } catch (IndexOutOfBoundsException e) {
            FirebaseCrash.logcat(Log.ERROR, LOG_TAG, "Chip not found.");
            FirebaseCrash.report(e);
        }
    }

    private void onChipInteraction(Chip chip, boolean nameClicked) {
        unselectChipsExcept(chip);
        if (chip.isSelected()) {
            mChipList.remove(chip);
            if (mChipsListener != null) {
                mChipsListener.onChipDeleted(chip);
            }
            onChipsChanged(true);
            if (nameClicked) {
                mEditText.setText(chip.getContact().getContent().toString());
                addLeadingMarginSpan();
                mEditText.requestFocus();
                mEditText.setSelection(mEditText.length());
            }
        } else {
            chip.setSelected(true);
            onChipsChanged(false);
        }
    }

    private void unselectChipsExcept(Chip rootChip) {
        for (Chip chip : mChipList) {
            if (chip != rootChip) {
                chip.setSelected(false);
            }
        }
        onChipsChanged(false);
    }

    private void unselectAllChips() {
        unselectChipsExcept(null);
    }
    //</editor-fold>

    //<editor-fold desc="InputConnectionWrapperInterface Implementation">
    @Override
    public InputConnection getInputConnection(InputConnection target) {
        return new KeyInterceptingInputConnection(target);
    }
    //</editor-fold>

    //<editor-fold desc="Inner Classes / Interfaces">
    private class EditTextListener implements TextWatcher {

        private boolean mIsPasteTextChange = false;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (count > 1) {
                mIsPasteTextChange = true;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mIsPasteTextChange) {
                mIsPasteTextChange = false;
                // todo handle copy/paste text here

            } else {
                // no paste text change
                if (s.toString().contains("\n")) {
                    String text = s.toString();
                    text = text.replace("\n", "");
                    while (text.contains("  ")) {
                        text = text.replace("  ", " ");
                    }
                    s.clear();
                    if (text.length() > 1) {
                        onEnterPressed(text);
                    } else {
                        s.append(text);
                    }
                }
            }
            if (mChipsListener != null) {
                mChipsListener.onTextChanged(s);
            }
        }
    }

    private class KeyInterceptingInputConnection extends InputConnectionWrapper {

        public KeyInterceptingInputConnection(InputConnection target) {
            super(target, true);
        }

        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            return super.commitText(text, newCursorPosition);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (mEditText.length() == 0) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                        selectOrDeleteLastChip();
                        return true;
                    }
                }
            }
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                mEditText.append("\n");
                return true;
            }

            return super.sendKeyEvent(event);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            // magic: in latest Android, deleteSurroundingText(1, 0) will be called for backspace
            if (mEditText.length() == 0 && beforeLength == 1 && afterLength == 0) {
                // backspace
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }

            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }

    public class Chip implements View.OnClickListener {

        private static final int MAX_LABEL_LENGTH = 30;

        private String mLabel;
        private final Uri mPhotoUri;
        private final Content mContent;
        private final boolean mIsIndelible;

        private RelativeLayout mView;
        private View mIconWrapper;
        private TextView mTextView;

        private ImageView mAvatarView;
        private ImageView mPersonIcon;
        private ImageView mCloseIcon;

        private ImageView mErrorIcon;

        private boolean mIsSelected = false;

        public Chip(String label, Uri photoUri, Content content) {
            this(label, photoUri, content, false);
        }

        public Chip(String label, Uri photoUri, Content content, boolean isIndelible) {
            this.mLabel = label;
            this.mPhotoUri = photoUri;
            this.mContent = content;
            this.mIsIndelible = isIndelible;

            if (mLabel == null) {
                mLabel = content.getContent().toString();
            }

            if (mLabel != null && mLabel.length() > MAX_LABEL_LENGTH) {
                mLabel = mLabel.substring(0, MAX_LABEL_LENGTH) + "...";
            }
        }

        public View getView() {
            if (mView == null) {
                mView = (RelativeLayout) inflate(getContext(), R.layout.chips_view, null);
                mView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (CHIP_HEIGHT * mDensity)));
                mAvatarView = (ImageView) mView.findViewById(R.id.ri_ch_avatar);
                mIconWrapper = mView.findViewById(R.id.rl_ch_avatar);
                mTextView = (TextView) mView.findViewById(R.id.tv_ch_name);
                mPersonIcon = (ImageView) mView.findViewById(R.id.iv_ch_person);
                mCloseIcon = (ImageView) mView.findViewById(R.id.iv_ch_close);

                mErrorIcon = (ImageView) mView.findViewById(R.id.iv_ch_error);

                // set inital res & attrs
                mView.setBackgroundResource(mChipsBgRes);
                mView.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.getBackground().setColorFilter(mChipsBgColor, PorterDuff.Mode.SRC_ATOP);
                    }
                });
                mIconWrapper.setBackgroundResource(R.drawable.circle);
                mTextView.setTextColor(mChipsTextColor);

                // set icon resources
                mPersonIcon.setBackgroundResource(mChipsPlaceholderResId);
                mCloseIcon.setBackgroundResource(mChipsDeleteResId);


                mView.setOnClickListener(this);
                mIconWrapper.setOnClickListener(this);
            }
            updateViews();
            return mView;
        }

        private void updateViews() {
            mTextView.setText(mLabel);
            if (mPhotoUri != null) {
                Picasso.with(getContext())
                        .load(mPhotoUri)
                        .noPlaceholder()
                        .into(mAvatarView, new Callback() {
                            @Override
                            public void onSuccess() {
                                mPersonIcon.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onError() {

                            }
                        });
            }
            if (isSelected()) {
                if (mChipsValidator != null && !mChipsValidator.isValid(mContent)) {
                    // not valid & show error
                    mView.getBackground().setColorFilter(mChipsBgColorErrorClicked, PorterDuff.Mode.SRC_ATOP);
                    mTextView.setTextColor(mChipsTextColorErrorClicked);
                    mIconWrapper.getBackground().setColorFilter(mChipsColorErrorClicked, PorterDuff.Mode.SRC_ATOP);
                    mErrorIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                } else {
                    mView.getBackground().setColorFilter(mChipsBgColorClicked, PorterDuff.Mode.SRC_ATOP);
                    mTextView.setTextColor(mChipsTextColorClicked);
                    mIconWrapper.getBackground().setColorFilter(mChipsColorClicked, PorterDuff.Mode.SRC_ATOP);
                }
                mPersonIcon.animate().alpha(0.0f).setDuration(200).start();
                mAvatarView.animate().alpha(0.0f).setDuration(200).start();
                mCloseIcon.animate().alpha(1f).setDuration(200).setStartDelay(100).start();

            } else {
                if (mChipsValidator != null && !mChipsValidator.isValid(mContent)) {
                    // not valid & show error
                    mErrorIcon.setVisibility(View.VISIBLE);
                    mErrorIcon.setColorFilter(null);
                } else {
                    mErrorIcon.setVisibility(View.GONE);
                }
                mView.getBackground().setColorFilter(mChipsBgColor, PorterDuff.Mode.SRC_ATOP);
                mTextView.setTextColor(mChipsTextColor);
                mIconWrapper.getBackground().setColorFilter(mChipsColor, PorterDuff.Mode.SRC_ATOP);

                mPersonIcon.animate().alpha(0.3f).setDuration(200).setStartDelay(100).start();
                mAvatarView.animate().alpha(1f).setDuration(200).setStartDelay(100).start();
                mCloseIcon.animate().alpha(0.0f).setDuration(200).start();
            }
        }

        @Override
        public void onClick(View v) {
            mEditText.clearFocus();
            if (v.getId() == mView.getId()) {
                onChipInteraction(this, true);
            } else {
                onChipInteraction(this, false);
            }
        }

        public boolean isSelected() {
            return mIsSelected;
        }

        public void setSelected(boolean isSelected) {
            if (mIsIndelible) {
                return;
            }
            this.mIsSelected = isSelected;
        }

        public Content getContact() {
            return mContent;
        }

        @Override
        public boolean equals(Object o) {
            if (mContent != null && o instanceof Content) {
                return mContent.equals(o);
            }
            return super.equals(o);
        }

        @Override
        public String toString() {
            return "{"
                    + "[Contact: " + mContent + "]"
                    + "[Label: " + mLabel + "]"
                    + "[PhotoUri: " + mPhotoUri + "]"
                    + "[IsIndelible" + mIsIndelible + "]"
                    + "}"
                    ;
        }
    }

    public interface ChipsListener {
        void onChipAdded(Chip chip);

        void onChipDeleted(Chip chip);

        void onTextChanged(CharSequence text);
    }

    public static abstract class ChipValidator {
        public abstract boolean isValid(Content content);
    }
    //</editor-fold>
}
