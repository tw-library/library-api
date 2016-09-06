package com.thoughtworks.librarysystem.copy;

import com.thoughtworks.librarysystem.loan.exceptions.EmailNotFoundException;
import com.thoughtworks.librarysystem.user.User;
import com.thoughtworks.librarysystem.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.List;

@RepositoryRestController
@RequestMapping("/copy")
public class CopyController {

    @Autowired
    private CopyService copyService;

    @Autowired
    private UserRepository userRepository;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Collection<Copy> listCopies(@Param("slug")  String slug, @Param("email") String email){
        List<User> users = userRepository.findByEmail(email);
        if (users.isEmpty()) {
            throw new EmailNotFoundException();
        }
        return copyService.findCopiesBySlugAndUser(slug, users.get(0));
    }
}