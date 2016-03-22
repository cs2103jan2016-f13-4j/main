package ui.view;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import storage.Task;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by maianhvu on 22/03/2016.
 */
public class TextListView extends View {


    public TextListView(List<Pair<Integer, Task>> data) {
        super(data);
    }

    @Override protected void buildContent() {
        assert this.getData() instanceof List;
        List<Pair<Integer, Task>> data = this.getData();

        Label dataLabel = new Label();
        AnchorPane.setTopAnchor(dataLabel, 0.0);
        AnchorPane.setBottomAnchor(dataLabel, 0.0);
        AnchorPane.setLeftAnchor(dataLabel, 0.0);
        AnchorPane.setRightAnchor(dataLabel, 0.0);

        StringBuilder sb = new StringBuilder();
        data.stream()
                .map(metadata -> {
                    int index = metadata.getKey();
                    Task task = metadata.getValue();
                    return String.format("%d. [%s] %s", index, task.getEndTime(), task.getTaskName());
                })
                .forEach(line -> {
                    sb.append(line);
                    sb.append("\n");
                });
        dataLabel.setText(sb.toString());

        this.setComponent(dataLabel);
    }
}
