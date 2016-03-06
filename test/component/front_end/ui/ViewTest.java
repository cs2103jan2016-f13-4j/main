package component.front_end.ui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ViewTest {
    private final static String STRING_EMPTY = "";
    private final static String STRING_DUMMY = "hello";
    private final static String STRING_NEW_LINE = "\n";
    
    private class TestView extends View<Object> {
        public TestView() {
            super(null);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void buildContent() {
            // TODO Auto-generated method stub
            
        }
    }
    
    
    TestView tView1;
    TestView tView2;
    String result;
    
    @Before 
    public void init(){
        result = STRING_EMPTY; 
        tView1 = new TestView();
        tView2 = new TestView(); 
    }
    @Test
    public void testAddText() {
        tView1.addText(STRING_DUMMY);
        assertThat(tView1.getContent(), is(STRING_DUMMY));
        tView1.addText(STRING_DUMMY);
        assertThat(tView1.getContent(), is(equalTo(STRING_DUMMY + STRING_DUMMY)));
        tView1.addText("");
        assertThat(tView1.getContent(), is(equalTo(STRING_DUMMY + STRING_DUMMY)));
    }
    
    @Test
    public void testAddLine() {
        tView1.addLine(STRING_DUMMY);
        tView2.addText(STRING_DUMMY);
        assertThat(tView1.getContent(), is(not(equalTo(tView2.getContent()))));
        tView2.addText(STRING_NEW_LINE);
        assertThat(tView2.getContent(),is(equalTo(tView1.getContent())));
    }

}
