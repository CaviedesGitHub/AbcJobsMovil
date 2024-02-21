package com.example.abcjobsnav;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;


import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
//import androidx.test.rule.ActivityTestRule;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class abcempresas {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityTestRule = new ActivityScenarioRule<>(MainActivity.class);

    //@Test
    public void Signup() {
        ViewInteraction signBtn = onView(allOf(withId(R.id.textButtonSignup), withText("Do not have an account? sign up"), isDisplayed()));
        signBtn.perform(click());

        ViewInteraction usernameTxt = onView(withId(R.id.userName));
        usernameTxt.perform(scrollTo(), replaceText("Luis_Edo10"), closeSoftKeyboard());

        ViewInteraction pwdTxt = onView(withId(R.id.password));
        pwdTxt.perform(scrollTo(), replaceText("User_10"), closeSoftKeyboard());

        ViewInteraction pwdTxt2 = onView(withId(R.id.confirmPassword));
        pwdTxt2.perform(scrollTo(), replaceText("User_10"), closeSoftKeyboard());

        ViewInteraction confirmSignupBtn = onView(allOf(withId(R.id.btnSignUp), withText("Submit")));
        confirmSignupBtn.perform(scrollTo(), click());
    }

    //@Test
    public void login() throws InterruptedException {
        ViewInteraction usernameTxt = onView(withId(R.id.txtUserName));
        usernameTxt.perform(replaceText("Luis_Edo10"), closeSoftKeyboard());

        ViewInteraction pwdTxt = onView(withId(R.id.txtPassword));
        pwdTxt.perform(replaceText("User_10"), closeSoftKeyboard());

        ViewInteraction confirmLoginBtn = onView(allOf(withId(R.id.btnLogin), withText("Submit")));
        confirmLoginBtn.perform(click());

        Thread.sleep(5000);
    }

    @Rule
    public ActivityScenarioRule<MainActivity> getmActivityTestRule() {
        return mActivityTestRule;
    }
    //@Test
    public void countTest() throws InterruptedException {
        getmActivityTestRule().getScenario().onActivity(
                activity -> {
                    RecyclerView recyclerView=activity.findViewById(R.id.puestosAsigRv);
                    final Integer ItemCount = recyclerView.getAdapter().getItemCount();
           }
        );
    }

    @Test
    public void e2eQualys() throws InterruptedException {
        ViewInteraction nameLogin = onView(withId(R.id.txtUserName));
        nameLogin.perform(replaceText("Daniel1"), closeSoftKeyboard());

        ViewInteraction pwdLogin = onView(withId(R.id.txtPassword));
        pwdLogin.perform(replaceText("12345"), closeSoftKeyboard());

        ViewInteraction confirmLoginBtn = onView(allOf(withId(R.id.btnLogin), withText("Submit")));
        confirmLoginBtn.perform(click());

        Thread.sleep(2000);

        //ViewInteraction BtnQualyCompany = onView(allOf(withId(R.id.btnLogin), withText("Submit")));
        ViewInteraction BtnQualyCompany = onView(allOf(withId(R.id.btnCalificaciones), withText(InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.company_qualifications))));
        BtnQualyCompany.perform(click());
        Thread.sleep(5000);

        //onView(withId(R.id.puestosAsigRv)).check(matches(hasChildCount(2)));

        //ViewInteraction vi = onView(withId(R.id.puestosAsigRv));
        //RecyclerView rv = (RecyclerView) vi;

        onView(new RecyclerViewMatcher(R.id.puestosAsigRv)
                .atPositionOnView(0, R.id.txtNum))
                .check(matches(withText("1")))
                .perform(click());
        Thread.sleep(5000);

        final Integer[] ItemCount = new Integer[1];
        getmActivityTestRule().getScenario().onActivity(
                activity -> {
                    RecyclerView recyclerView=activity.findViewById(R.id.evalsPuestoAsigRv);
                    int countBefore= Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();
                    Log.d("Testing countBefore", String.valueOf(countBefore));
                    if (recyclerView!=null) {
                        if (recyclerView.getAdapter()!=null){
                            ItemCount[0] = recyclerView.getAdapter().getItemCount();
                        }
                        else{
                            ItemCount[0]=0;
                        }
                    }
                    else{
                        ItemCount[0]=0;
                    }
                }
        );
        System.out.println("ItemCount: "+ ItemCount[0]);
        Thread.sleep(2000);

        ViewInteraction BtnAddEvaluation = onView(allOf(withId(R.id.btnAddEvaluation), withText(InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.evals_add))));
        BtnAddEvaluation.perform(click());
        Thread.sleep(2000);

        //onView(withText("Excellent"))
        //        .inRoot(RootMatchers.isPlatformPopup())
        //        .perform(click());
        //ViewInteraction qualyEval = onView(withId(R.id.AutoComplete1)); //AutoComplete1
        //qualyEval.perform(replaceText("Excellent"), closeSoftKeyboard());

        Thread.sleep(1000);
        ViewInteraction notaEval = onView(withId(R.id.txtNota));  //txtNota
        notaEval.perform(replaceText("Notas de la evaluacion."), closeSoftKeyboard());
        Thread.sleep(1000);

        ViewInteraction BtnCreateCreaEval = onView(allOf(withId(R.id.btnCreateEval), withText("Create")));
        BtnCreateCreaEval.perform(ViewActions.scrollTo()).perform(click());

        //ViewInteraction BtnCancelCreaEval = onView(allOf(withId(R.id.btnCancelCreateEval), withText(InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.creaeval_cancel))));
        //BtnCancelCreaEval.perform(click());
        Thread.sleep(5000);

        final Integer[] ItemCount2 = new Integer[1];
        getmActivityTestRule().getScenario().onActivity(
                activity -> {
                    RecyclerView recyclerView=activity.findViewById(R.id.evalsPuestoAsigRv);
                    if (recyclerView!=null) {
                        if (recyclerView.getAdapter()!=null){
                            ItemCount2[0] = recyclerView.getAdapter().getItemCount();
                        }
                        else{
                            ItemCount2[0]=0;
                        }
                    }
                    else{
                        ItemCount2[0]=0;
                    }
                }
        );
        System.out.println("ItemCount2: "+ ItemCount2[0]);
        Thread.sleep(2000);

        Integer sum = ItemCount[0]+1;
        Log.d("Testing valor1", String.valueOf(sum));
        Log.d("Testing valor2", String.valueOf(ItemCount2[0]));
        assertEquals(sum, ItemCount2[0]);

        onView(withId(R.id.evalsPuestoAsigRv))
                // Instead of 'not', you can use any other hamcrest Matcher like 'is', 'lessThan' etc.
                .check(new RecyclerViewItemCountAssertion(not(ItemCount[0])));

        ViewInteraction BtnEvalsBackJobs = onView(allOf(withId(R.id.btnBackListJobs), withText(InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.evals_back_jobs_list))));
        BtnEvalsBackJobs.perform(ViewActions.scrollTo()).perform(click());

        ViewInteraction BtnQualyBackCompany = onView(allOf(withId(R.id.btnQualyBackCompany), withText(InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.back_to_company))));
        BtnQualyBackCompany.perform(ViewActions.scrollTo()).perform(click());
    }

    @Test
    public void e2eAsigna() throws InterruptedException {
        ViewInteraction nameLogin = onView(withId(R.id.txtUserName));
        nameLogin.perform(replaceText("Daniel1"), closeSoftKeyboard());

        ViewInteraction pwdLogin = onView(withId(R.id.txtPassword));
        pwdLogin.perform(replaceText("12345"), closeSoftKeyboard());

        ViewInteraction confirmLoginBtn = onView(allOf(withId(R.id.btnLogin), withText("Submit")));
        confirmLoginBtn.perform(click());

        Thread.sleep(2000);

        ViewInteraction BtnAsignaCompany = onView(allOf(withId(R.id.btnAsignacion), withText(InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.company_assignment))));
        BtnAsignaCompany.perform(ViewActions.scrollTo()).perform(click());

        Thread.sleep(2000);

        final Integer[] ItemCountPuestosAsig = new Integer[1];
        getmActivityTestRule().getScenario().onActivity(
                activity -> {
                    RecyclerView recyclerView=activity.findViewById(R.id.puestosSinAsigRv);
                    ItemCountPuestosAsig[0]= Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();
                    Log.d("Testing countBefore", String.valueOf(ItemCountPuestosAsig[0]));
                }
        );
        if (ItemCountPuestosAsig[0]==0){
            ViewInteraction BtnAsignaBackCompany = onView(allOf(withId(R.id.btnBackCompany), withText(InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.back_to_company))));
            BtnAsignaBackCompany.perform(ViewActions.scrollTo()).perform(click());
        }
        else{
            final String[] texto = {""};
            getmActivityTestRule().getScenario().onActivity(
                    activity -> {
                        EditText et = activity.findViewById(R.id.editTextText2);
                        texto[0] = et.getText().toString();
                        Log.d("Testing texto", texto[0]);
                    }
            );
            String[] arrOfStr = texto[0].split(" ");
            Integer totalRegIni=Integer.valueOf(arrOfStr[2]);

            String[] arrOfStr2 = arrOfStr[0].split("-");

            onView(new RecyclerViewMatcher(R.id.puestosSinAsigRv)
                    .atPositionOnView(0, R.id.txtNum))
                    .check(matches(withText(arrOfStr2[0])))
                    .perform(click());
            Thread.sleep(5000);

            final Integer[] ItemCountCumplen = new Integer[1];
            getmActivityTestRule().getScenario().onActivity(
                    activity -> {
                        RecyclerView recyclerView=activity.findViewById(R.id.cumplenRv);
                        ItemCountCumplen[0]= Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();
                        Log.d("Testing countBefore", String.valueOf(ItemCountCumplen[0]));
                    }
            );
            if (ItemCountCumplen[0]==0){
                ViewInteraction BtnCumplenBackAsignedJob = onView(allOf(withId(R.id.btnCancelCumplen), withText(InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.cumplen_back))));
                BtnCumplenBackAsignedJob.perform(ViewActions.scrollTo()).perform(click());
                Thread.sleep(500);
                ViewInteraction BtnAsignaBackCompany = onView(allOf(withId(R.id.btnBackCompany), withText(InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.back_to_company))));
                BtnAsignaBackCompany.perform(ViewActions.scrollTo()).perform(click());
            }
            else{
                onView(new RecyclerViewMatcher(R.id.cumplenRv)
                        .atPositionOnView(0, R.id.lblCandidatoCumple))
                        .check(matches(withText(InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.cumplen_candidate))))
                        .perform(click());
                Thread.sleep(5000);

                ViewInteraction BtnAsignaCumplen = onView(allOf(withId(R.id.btnAsignaCumplen), withText(InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.cumplen_assign))));
                BtnAsignaCumplen.perform(ViewActions.scrollTo()).perform(click());

                final String[] textoFin = {""};
                getmActivityTestRule().getScenario().onActivity(
                        activity -> {
                            EditText et = activity.findViewById(R.id.editTextText2);
                            textoFin[0] = et.getText().toString();
                            Log.d("Testing texto", textoFin[0]);
                        }
                );
                String[] arrOfStrFin = textoFin[0].split(" ");
                Integer totalRegFin=Integer.valueOf(arrOfStrFin[2]);
                totalRegFin=totalRegFin+1;
                assertEquals(totalRegIni, totalRegFin);

                Thread.sleep(500);
                ViewInteraction BtnAsignaBackCompany = onView(allOf(withId(R.id.btnBackCompany), withText(InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.back_to_company))));
                BtnAsignaBackCompany.perform(ViewActions.scrollTo()).perform(click());
            }
        }

    }

    @Test
    public void e2eEntrevista() throws InterruptedException {
        ViewInteraction nameLogin = onView(withId(R.id.txtUserName));
        nameLogin.perform(replaceText("Daniel1"), closeSoftKeyboard());

        ViewInteraction pwdLogin = onView(withId(R.id.txtPassword));
        pwdLogin.perform(replaceText("12345"), closeSoftKeyboard());

        ViewInteraction confirmLoginBtn = onView(allOf(withId(R.id.btnLogin), withText("Submit")));
        confirmLoginBtn.perform(click());

        Thread.sleep(2000);

        ViewInteraction BtnEntrevistasCompany = onView(allOf(withId(R.id.btnEntrevistasEmp), withText(InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.empresa_interviews))));
        BtnEntrevistasCompany.perform(ViewActions.scrollTo()).perform(click());

        Thread.sleep(4000);

        final Integer[] ItemCountEntrevistas = new Integer[1];
        getmActivityTestRule().getScenario().onActivity(
                activity -> {
                    RecyclerView recyclerView=activity.findViewById(R.id.entrevistasRvEVC);
                    ItemCountEntrevistas[0]= Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();
                    Log.d("Testing countBefore", String.valueOf(ItemCountEntrevistas[0]));
                }
        );

        if (ItemCountEntrevistas[0]==0){
            //ViewInteraction BtnEntrevistaBackCompany = onView(allOf(withId(R.id.btnEvBackCompany), withText(InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.back_to_company))));
            ViewInteraction BtnEntrevistaBackCompany = onView(allOf(withId(R.id.btnEvBackCompany), withText("Back to Company")));
            BtnEntrevistaBackCompany.perform(ViewActions.scrollTo()).perform(click());
        }
        else{
            final String[] textoCandidato = {""};
            getmActivityTestRule().getScenario().onActivity(
                    activity -> {
                        RecyclerView recyclerView=activity.findViewById(R.id.entrevistasRvEVC);
                        //View view = recyclerView.getRootView();
                        View view = recyclerView.getChildAt(0);
                        //RecyclerView.Adapter adapter= recyclerView.getAdapter();
                        TextView texto = view.findViewById(R.id.txtCandidateEVC);
                        Log.d("Testing 29Espresso candidato", textoCandidato[0]);
                        textoCandidato[0]= texto.getText().toString();
                        Log.d("Testing 3Espresso candidato", textoCandidato[0]);
                    }
            );

            onView(new RecyclerViewMatcher(R.id.entrevistasRvEVC)
                    .atPositionOnView(0, R.id.txtNum))
                    .check(matches(withText("1")))
                    .perform(click());
            Thread.sleep(5000);

            final String[] textoCandidato2 = {""};
            getmActivityTestRule().getScenario().onActivity(
                    activity -> {
                        TextView et = activity.findViewById(R.id.txtCandidateEvResult);
                        textoCandidato2[0] = et.getText().toString();
                        Log.d("Testing texto", textoCandidato2[0]);
                    }
            );

            assertEquals(textoCandidato[0], textoCandidato2[0]);

            ViewInteraction BtnBackFromEV = onView(allOf(withId(R.id.btnBackFromEV), withText(InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.ResultEV_back))));
            BtnBackFromEV.perform(ViewActions.scrollTo()).perform(click());

            ViewInteraction BtnEntrevistaBackCompany = onView(allOf(withId(R.id.btnEvBackCompany), withText(InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.back_to_company))));
            BtnEntrevistaBackCompany.perform(ViewActions.scrollTo()).perform(click());
        }
        Thread.sleep(2000);
    }

    public class RecyclerViewItemCountAssertion implements ViewAssertion {

        private final Matcher<Integer> matcher;

        public RecyclerViewItemCountAssertion(int expectedCount) {
            this.matcher = is(expectedCount);
        }

        public RecyclerViewItemCountAssertion(Matcher<Integer> matcher) {
            this.matcher = matcher;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assert adapter != null;
            assertThat(adapter.getItemCount(), matcher);
        }
    }

    public class RecyclerViewMatcher {
        private final int recyclerViewId;

        public RecyclerViewMatcher(int recyclerViewId) {
            this.recyclerViewId = recyclerViewId;
        }

        public Matcher<View> atPosition(final int position) {
            return atPositionOnView(position, -1);
        }

        public Matcher<View> atPositionOnView(final int position, final int targetViewId) {

            return new TypeSafeMatcher<View>() {
                Resources resources = null;
                View childView;

                public void describeTo(Description description) {
                    String idDescription = Integer.toString(recyclerViewId);
                    if (this.resources != null) {
                        try {
                            idDescription = this.resources.getResourceName(recyclerViewId);
                        } catch (Resources.NotFoundException var4) {
                            idDescription = String.format("%s (resource name not found)",
                                    new Object[] { Integer.valueOf
                                            (recyclerViewId) });
                        }
                    }

                    description.appendText("with id: " + idDescription);
                }

                public boolean matchesSafely(View view) {

                    this.resources = view.getResources();

                    if (childView == null) {
                        RecyclerView recyclerView =
                                (RecyclerView) view.getRootView().findViewById(recyclerViewId);
                        if (recyclerView != null && recyclerView.getId() == recyclerViewId) {
                            childView = recyclerView.findViewHolderForAdapterPosition(position).itemView;
                        }
                        else {
                            return false;
                        }
                    }

                    if (targetViewId == -1) {
                        return view == childView;
                    } else {
                        View targetView = childView.findViewById(targetViewId);
                        return view == targetView;
                    }

                }
            };
        }
    }
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }
            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup &&
                        parentMatcher.matches(parent)&& view.equals(((ViewGroup)
                        parent).getChildAt(position));
            }
        };
    }
}