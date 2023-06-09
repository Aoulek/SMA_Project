package sma.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import javafx.application.Platform;
import sma.ReservationContainer;
import sma.behavouirs.ReservationBehaviour;

public class ReservationAgent extends Agent {

	ParallelBehaviour parallelBehaviour;
	int requesterCount;
	ReservationContainer gui;

	@Override
	protected void setup() {
		gui = (ReservationContainer) getArguments()[0];
		gui.setManagerAgent(this);
		System.out.println(" je suis l'agent responsable  de réservation mon nom est:" + this.getAID().getName());
		parallelBehaviour = new ParallelBehaviour();

		addBehaviour(parallelBehaviour);
		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
			@Override
			public void action() {
				MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
				ACLMessage aclMessage = receive(template);

				if (aclMessage != null) {
					String reservation = aclMessage.getContent();
					AID requester = aclMessage.getSender();
					++requesterCount;

					GuiEvent guiEvent = new GuiEvent(this, 1);
					guiEvent.addParameter(reservation);
					Platform.runLater(() -> {
						gui.insertMessage(guiEvent);
					});

					String conversationID = "transaction_" + reservation + "_" + requesterCount;

					parallelBehaviour.addSubBehaviour(
							new ReservationBehaviour(myAgent, reservation, requester, conversationID, gui));
				} else
					block();
			}
		});
	}

	@Override
	protected void beforeMove() {
		System.out.println("Avant de migrer vers une nouvelle location.....");
	}

	@Override
	protected void afterMove() {
		System.out.println("Je viens d'arriver � une nouvelle location.....");
	}

	@Override
	protected void takeDown() {
		System.out.println("avant de mourir .....");
	}
}
