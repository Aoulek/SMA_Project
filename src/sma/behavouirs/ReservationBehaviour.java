package sma.behavouirs;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import javafx.application.Platform;
import sma.ReservationContainer;
import sma.agents.RestaurantAgent;

public class ReservationBehaviour extends CyclicBehaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String conversationID;
	private AID requester;
	private String restaurant;
	private double capacite;
	private int compteur;
	private List<AID> listRestaurants = new ArrayList<>();
	private AID meilleureOffre;
	private double meilleurcapacite;
	private int index;
	ReservationContainer gui;

	public ReservationBehaviour(Agent agent, String restaurant, AID requester, String conversationID,
			ReservationContainer gui) {
		super(agent);
		;
		this.restaurant = restaurant;
		this.requester = requester;
		this.conversationID = conversationID;
		this.gui = gui;

		System.out.println("Recherche des services...");

		listRestaurants = chercherServices(myAgent, "offre-restaurant");
		System.out.println("Liste des Restaurants trouvés :");
		try {
			for (AID aid : listRestaurants) {
				System.out.println("====" + aid.getName());
			}
			++compteur;

			GuiEvent guiEvent = new GuiEvent(this, 1);
			guiEvent.addParameter(" Il ya une demande  de réservation de :"+ restaurant );
			guiEvent.addParameter(" faite par l'agent :" + requester.getLocalName());

			
			Platform.runLater(() -> {
				gui.insertMessage(guiEvent);
			});
			// gui.insertMessage(guiEvent);
			System.out.println("#########################################");
			System.out.println("Il ya une demande  de réservation de restaurant:");
			System.out.println(" faite par l'agent :" + requester.getName());
			System.out.println("le restaurant : " + restaurant);
			System.out.println("............................");
			System.out.println("Envoi de la requ�te....");

			ACLMessage msg = new ACLMessage(ACLMessage.CFP);
			msg.setContent(restaurant);
			msg.setConversationId(conversationID);
			msg.addUserDefinedParameter("compteur", String.valueOf(compteur));

			for (AID aid : listRestaurants) {
				msg.addReceiver(aid);
			}

			System.out.println("....... En cours");

			Thread.sleep(5000);
			index = 0;
			myAgent.send(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void action() {
		try {
			MessageTemplate template = MessageTemplate.and(MessageTemplate.MatchConversationId(conversationID),
					MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
							MessageTemplate.MatchPerformative(ACLMessage.CONFIRM)));

			ACLMessage aclMessage = myAgent.receive(template);

			if (aclMessage != null) {
				switch (aclMessage.getPerformative()) {

				case ACLMessage.PROPOSE:
					capacite = Double.parseDouble(aclMessage.getContent());
					GuiEvent guiEvent = new GuiEvent(this, 1);
					guiEvent.addParameter("**** Communication avec L'agent Restaurant *****");
					guiEvent.addParameter(" Restaurant disponible :");
					guiEvent.addParameter( " Nom :" +aclMessage.getSender().getLocalName()+" ,Capacité : "+ capacite);
			

					Platform.runLater(() -> {
						gui.insertMessage(guiEvent);
					});
					// gui.insertMessage(guiEvent);
					System.out.println("**********  Communication avec L'agent Restaurant *******");
					System.out.println("Conversation ID:" + aclMessage.getConversationId());
					System.out.println("Restaurant disponible :");
					System.out.println( aclMessage.getSender().getLocalName());
					System.out.println("La Capacité : " + capacite);

					if (index == 0) {
						meilleurcapacite = capacite;
						meilleureOffre = aclMessage.getSender();
					} else {
						if (capacite > meilleurcapacite) {
							meilleurcapacite = capacite;
							meilleureOffre = aclMessage.getSender();
						}
					}
					++index;
					if (index == listRestaurants.size()) {
						index = 0;
						System.out.println("-----------------------------------");
						System.out.println("Conclusion de la transaction.......");
						ACLMessage aclMessage2 = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
						aclMessage2.addReceiver(meilleureOffre);
						aclMessage2.setConversationId(conversationID);
						System.out.println("...... En cours");
						Thread.sleep(5000);
						myAgent.send(aclMessage2);
					}
					break;
				case ACLMessage.CONFIRM:
					System.out.println(".........................");
					System.out.println("Recu de la confirmation ...");
					System.out.println("Conversation ID:" + aclMessage.getConversationId());
					ACLMessage msg3 = new ACLMessage(ACLMessage.INFORM);
					msg3.addReceiver(requester);
					msg3.setConversationId(conversationID);

					msg3.setContent("Le restaurant : " + restaurant + "\n" 
			                + "a une capacite : " + meilleurcapacite + "\n"
			                + " un Nbr de place disponible : " + aclMessage.getContent() + "\n"
			                + "Le restaurant qui a une meuilleur capacité est : " + aclMessage.getSender().getLocalName());
			myAgent.send(msg3);

					break;
				}

			} else {
				block();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<AID> chercherServices(Agent agent, String type) {

		List<AID> listRestaurants = new ArrayList<>();

		DFAgentDescription agentDescription = new DFAgentDescription();
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType(type);
		agentDescription.addServices(serviceDescription);

		try {
			DFAgentDescription[] descriptions = DFService.search(agent, agentDescription);

			for (DFAgentDescription dfad : descriptions) {
				listRestaurants.add(dfad.getName());
			}
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		return listRestaurants;
	}
}
