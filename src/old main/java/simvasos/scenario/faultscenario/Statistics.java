package simvasos.scenario.faultscenario;

import simvasos.simulation.analysis.Snapshot;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Statistics {
    class StatisticsNode {
        int nTrial;
        String sosType;
        int pCount;     // 환자 수
        int ffCount;    // 소방관 수
        int delay;
        ArrayList<Snapshot> trace;

        public StatisticsNode(int nTrial, String sosType, int pCount, int ffCount, int delay, ArrayList<Snapshot> trace) {
            this.nTrial = nTrial;
            this.sosType = sosType;
            this.pCount = pCount;
            this.ffCount = ffCount;
            this.delay = delay;
            this.trace = trace;
        }
    }

    ArrayList<StatisticsNode> nodes = new ArrayList<StatisticsNode>();

    public void add(StatisticsNode node) {
        nodes.add(node);
    }
    public void add(int nTrial, String sosType, int pCount, int ffCount, int delay, ArrayList<Snapshot> trace) {
        add(new StatisticsNode(nTrial, sosType, pCount, ffCount, delay, trace));
    }

    public void write(BufferedWriter writer) throws IOException {
//        for (Snapshot snapshot : trace) {
//            bw.write((snapshot.getProperties().get(0).value).toString());
//            bw.newLine();
//        }


        for(int i = 0; i < nodes.size(); ++i) {
            StatisticsNode node = nodes.get(i);
            String content = node.sosType + node.nTrial;
            if(i < nodes.size() - 1) {
                content += ", ";
            }

            if(i < nodes.size() - 1) {
                if(nodes.get(i).nTrial != nodes.get(i+1).nTrial) {
                    content += " ,";
                }
            }
            writer.write(content);
        }
        writer.newLine();

        for(int i = 0; i < nodes.size(); ++i) {
            StatisticsNode node = nodes.get(i);
            String content = "P: " + String.valueOf(node.pCount) + " / FF: " + String.valueOf(node.ffCount);
            if(i < nodes.size() - 1) {
                content += ", ";
            }

            if(i < nodes.size() - 1) {
                if(nodes.get(i).nTrial != nodes.get(i+1).nTrial) {
                    content += " ,";
                }
            }
            writer.write(content);
        }
        writer.newLine();

        for(int i = 0; i < nodes.size(); ++i) {
            StatisticsNode node = nodes.get(i);
            String content = "delay: " + String.valueOf(node.delay);
            if(i < nodes.size() - 1) {
                content += ", ";
            }

            if(i < nodes.size() - 1) {
                if(nodes.get(i).nTrial != nodes.get(i+1).nTrial) {
                    content += " ,";
                }
            }
            writer.write(content);
        }
        writer.newLine();

        int maxSize = -1;
        for(StatisticsNode node : nodes) {
            if(node.trace.size() > maxSize) {
                maxSize = node.trace.size();
            }
        }

        for(int trace_index = 0; trace_index < maxSize; trace_index++) {
            for (int i = 0; i < nodes.size(); ++i) {
                StatisticsNode node = nodes.get(i);
                ArrayList<Snapshot> trace = node.trace;
                String content = "";
                try{
                    Snapshot snapshot = trace.get(trace_index);
                    content = (snapshot.getProperties().get(0).value).toString();
                } catch (IndexOutOfBoundsException e) { }

                // 각 콘텐트 사이 열 구분
                if (i < nodes.size() - 1) {
                    content += ", ";
                }

                // nTrial이 바뀔 때 열 하나 건너뛰기
                if(i < nodes.size() - 1) {
                    if(nodes.get(i).nTrial != nodes.get(i+1).nTrial) {
                        content += ", ";
                    }
                }
                writer.write(content);
            }
            writer.newLine();
        }
        //writer.newLine();

        writer.flush();
    }
}
