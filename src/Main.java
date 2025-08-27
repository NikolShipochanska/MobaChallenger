import java.util.*;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();
        Map<String,Map<String,Integer>> pool = new LinkedHashMap<>();
        Map<String,Integer> totalPoints = new LinkedHashMap<>();

        while(!input.equals("Season end")){
            if(input.contains("->")){
                String[] inputData = input.split(" -> ");
                String player = inputData[0];
                String position = inputData[1];
                int skill = Integer.parseInt(inputData[2]);
                if(!pool.containsKey(player)){
                    pool.put(player,new LinkedHashMap<>());
                    pool.get(player).put(position,skill);
                }else{
                    if(!pool.get(player).containsKey(position)){
                        pool.get(player).put(position,skill);
                    }else {
                        int currentSkillValue = pool.get(player).get(position);
                        if (currentSkillValue < skill) {
                            pool.get(player).replace(position, currentSkillValue, skill);
                        } else {
                            pool.get(player).put(position, skill);
                        }
                    }
                }
            }else if(input.contains("vs")){
                String[] inputData = input.split(" vs ");
                String firstPlayer = inputData[0];
                String secondPlayer = inputData[1];
                boolean hasInCommon = false;
                if(pool.containsKey(firstPlayer) && pool.containsKey(secondPlayer)){
                    int firstPlayerTotal = 0;
                    int secondPlayerTotal = 0;
                    for(Map.Entry<String,Integer> firstEntry : pool.get(firstPlayer).entrySet()){
                        for(Map.Entry<String,Integer> secondEntry : pool.get(secondPlayer).entrySet()){
                            if(firstEntry.getKey().equals(secondEntry.getKey())){
                                hasInCommon = true;
                                break;
                            }
                        }
                    }

                    if(hasInCommon){
                        for(int points : pool.get(firstPlayer).values()){
                            firstPlayerTotal+=points;
                        }

                        for(int points : pool.get(secondPlayer).values()){
                            secondPlayerTotal+=points;
                        }

                        if(firstPlayerTotal > secondPlayerTotal){
                            pool.remove(secondPlayer);
                        }else if(secondPlayerTotal > firstPlayerTotal){
                            pool.remove(firstPlayer);
                        }
                    }
                }
            }
            input = scanner.nextLine();
        }

        for(Map.Entry<String,Map<String,Integer>> entry : pool.entrySet()){
            int currentPlayerTotalSkill = entry.getValue().values().stream().mapToInt(Integer::intValue).sum();
            totalPoints.put(entry.getKey(), currentPlayerTotalSkill);
        }

       totalPoints.entrySet().stream()
               .sorted(Map.Entry.<String,Integer> comparingByValue(Comparator.reverseOrder())
                       .thenComparing(Map.Entry::getKey))
               .forEach(e->{
                   String player = e.getKey();
                   int total = e.getValue();
                   System.out.println(player + ": " + total + " skill");

                   pool.get(player).entrySet().stream()
                           .sorted(Map.Entry.<String,Integer>comparingByValue(Comparator.reverseOrder())
                                   .thenComparing(Map.Entry::getKey))
                           .forEach(pos-> System.out.println("- " + pos.getKey() + " <::> " + pos.getValue()));
               });
    }
}