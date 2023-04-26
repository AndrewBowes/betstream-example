package org.apache.flink.streaming.examples.liability.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.apache.flink.streaming.examples.liability.data.Reason.ORIGINAL_VALUES;
import static org.apache.flink.streaming.examples.liability.data.Reason.PARTIAL_CASHOUT;


public class BetEvent {
    public static BetEvent[] betEvents = generateRandomBetEvents(1, 5);

    public Bet bet;
    public BetState state;
    public int flag; // This flag will determine if the created bet gets settled (0), cashed out (1), partial cashout (2)

    private static BetEvent createBetEvent(
            int betId, int selectionId, float stake, BetAction betAction, float potentialWin, String destination, String currency

    ) {
        BetEvent betEvent = new BetEvent();


        switch (betAction) {
            case PLACEMENT:
                betEvent.bet = new Bet(
                        betId, selectionId, stake, stake, 0, potentialWin, destination,
                        currency, BetStatus.ACTIVE, BetResult.NONE, Collections.emptyList()
                );
                betEvent.state = BetState.ACTIVE;
                break;
            case SETTLEMENT:
                betEvent.bet = new Bet(
                        betId, selectionId, stake, stake, 0, potentialWin, destination,
                        currency, BetStatus.SETTLED, BetResult.LOST, Collections.emptyList()
                );
                betEvent.state = BetState.FINAL;
                break;
            case CASHOUT:
                betEvent.bet = new Bet(
                        betId, selectionId, stake, stake, stake, potentialWin, destination,
                        currency, BetStatus.SETTLED, BetResult.CASHED_OUT, Collections.emptyList()
                );
                betEvent.state = BetState.FINAL;
                break;
            case PARTIAL_CASHOUT:
                betEvent.bet = new Bet(
                        betId, selectionId, stake, stake / 2, potentialWin / 6, potentialWin, destination,
                        currency, BetStatus.ACTIVE, BetResult.NONE, buildChangeInTerms()
                );
                betEvent.state = BetState.ACTIVE;
                break;
        }

        int flag =  new Random().nextInt(3);

        betEvent.flag = flag;
        return betEvent;
    }

    private static BetEvent[] generateRandomBetEvents(int count, int selectionCount) {
        BetEvent[] betEvents = new BetEvent[count];
        String[] currencyCodes = new String[] { "cad", "gbp", "eur", "jpy" };
        String[] destinations = new String[] { "ca", "ny", "wa", "fl" };
        Random random = new Random();

        int stake = random.nextInt(100);
        int currentCurrencyCodeIndex = 0;
        int currentDestinationIndex = 0;

        // create placed bet messages
        for(int i = 0; i < count; i+=1) {
            betEvents[i] = createBetEvent(
                    random.nextInt(99999),
                    random.nextInt(selectionCount),
                    stake,
                    BetAction.PLACEMENT,
                    (float) (stake * 1.5),
                    destinations[currentDestinationIndex],
                    currencyCodes[currentCurrencyCodeIndex]
                    );

            currentCurrencyCodeIndex += 1;
            if(currentCurrencyCodeIndex >= currencyCodes.length) {
                currentCurrencyCodeIndex = 0;
            }

            currentDestinationIndex += 1;
            if(currentDestinationIndex >= destinations.length) {
                currentDestinationIndex = 0;
            }
        }


        List<BetEvent> placementMessages = Arrays.stream(betEvents).collect(Collectors.toList());
        List<BetEvent> settlementMessages = Arrays.stream(betEvents).filter(betEvent -> betEvent.flag == 0)
                .map( betEvent -> createBetEvent(
                        betEvent.bet.betId,
                        betEvent.bet.selectionId,
                        stake,
                        BetAction.SETTLEMENT,
                        (float) (stake * 1.5),
                        betEvent.bet.destination,
                        betEvent.bet.currency
                )).collect(Collectors.toList());

        List<BetEvent> cashoutMessages = Arrays.stream(betEvents).filter(betEvent -> betEvent.flag == 1)
                .map( betEvent -> createBetEvent(
                        betEvent.bet.betId,
                        betEvent.bet.selectionId,
                        stake,
                        BetAction.CASHOUT,
                        (float) (stake * 1.5),
                        betEvent.bet.destination,
                        betEvent.bet.currency
                )).collect(Collectors.toList());

        List<BetEvent> pcoMessages = Arrays.stream(betEvents).filter(betEvent -> betEvent.flag == 2)
                .map( betEvent -> createBetEvent(
                        betEvent.bet.betId,
                        betEvent.bet.selectionId,
                        stake,
                        BetAction.PARTIAL_CASHOUT,
                        (float) (stake * 1.2),
                        betEvent.bet.destination,
                        betEvent.bet.currency
                )).collect(Collectors.toList());


        placementMessages.addAll(cashoutMessages);
        placementMessages.addAll(pcoMessages);
        placementMessages.addAll(settlementMessages);

        BetEvent[] itemsArray = new BetEvent[placementMessages.size()];
        itemsArray = placementMessages.toArray(itemsArray);

        return itemsArray;
    }

    public static List<ChangeInTerms> buildChangeInTerms() {
        return Arrays.asList(
                new ChangeInTerms(0, ORIGINAL_VALUES),
                new ChangeInTerms(1, PARTIAL_CASHOUT)
        );
    }
}
