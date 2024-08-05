package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntryEntity;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    private UserService userService;

    //It will create Transactional component
    //PlatformTransactionManager used By MongoTransactionManager
    @Transactional
    public void saveEntry(JournalEntryEntity journalEntry, String userName){
        try {
            User user = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntryEntity saved = journalEntryRepo.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveEntry(user);
        }catch (Exception e){
            System.out.println("Exception " + e);
            throw new RuntimeException("An Error Occured",e);
        }
    }
    public void saveEntry(JournalEntryEntity journalEntry){
        try {
            JournalEntryEntity saved = journalEntryRepo.save(journalEntry);
        }catch (Exception e){
            System.out.println("Exception " + e);
        }
    }

    public List<JournalEntryEntity> getAll(){
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntryEntity> findById(ObjectId id){
        return journalEntryRepo.findById(id);
    }

    public void deleteById(ObjectId id, String userName){
        User user = userService.findByUserName(userName);
        user.getJournalEntries().removeIf(x -> x.getId().equals(id));
        userService.saveEntry(user);
        journalEntryRepo.deleteById(id);
    }
}
//controller --> Service --> Repository