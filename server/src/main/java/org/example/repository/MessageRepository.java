package org.example.repository;

import org.example.model.Message;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class MessageRepository {

    private volatile static MessageRepository instance;

    private SessionFactory sessionFactory;

    private MessageRepository() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static MessageRepository getInstance() {
        if (instance == null) {
            synchronized (MessageRepository.class) {
                if (instance == null) {
                    instance = new MessageRepository();
                }
            }
        }
        return instance;
    }

    public void saveMessage(Message message) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(message);
        session.getTransaction().commit();
        session.close();
    }

    public List<Message> getAllMessages(String from, String to) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Message> result = session
                .createQuery("from Message where (frm = :from and to = :to) or (frm = :to and to = :from)", Message.class)
                .setParameter("from", from)
                .setParameter("to", to)
                .list();
        session.getTransaction().commit();
        session.close();
        return result;
    }
}
