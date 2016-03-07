package entity.command;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by maianhvu on 6/3/16.
 */
public class InstructionTest {

    @Test
    public void Instructions_without_quantifier_become_universal_if_their_type_allows() {
        for (Instruction.Type type : Instruction.Type.values()) {
            // Skip types that cannot have universal quantifiers
            if (!type.isUniversallyQuantifiable || type.doesRequireQuantifier) {
                continue;
            }

            // Instantiate an instruction with only the type and expect it to automatically
            // become universally quantified
            Instruction inst = new Instruction(type);

            assertThat(inst.isUniversallyQuantified(), is(true));
        }
    }

    @Test
    public void Instructions_with_integer_quantifier_are_not_universally_quantified() {
        for (Instruction.Type type : Instruction.Type.values()) {
            Instruction inst = new Instruction(type, 5);

            assertThat(inst.isUniversallyQuantified(), is(false));
        }
    }

    @Test
    public void Instructions_without_quantifier_but_required_quantifier_are_invalid() {
        for (Instruction.Type type : Instruction.Type.values()) {
            // Skip types that do not require quantifier and are automatically
            // universally quantifiable
            if (!type.doesRequireQuantifier || type.isUniversallyQuantifiable) {
                continue;
            }

            Instruction inst = new Instruction(type);
            assertThat(inst.getType(), is(Instruction.Type.INVALID));
        }
    }
}
