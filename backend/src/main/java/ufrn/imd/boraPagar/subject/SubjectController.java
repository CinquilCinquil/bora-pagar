package ufrn.imd.boraPagar.subject;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import ufrn.imd.boraPagar.core.AbstractController;
import ufrn.imd.boraPagar.core.Views;

import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("subjects")
public class SubjectController extends AbstractController<SubjectModel, SubjectService>{

    @JsonView(Views.Public.class)
    @PostMapping("/interested")
    public ResponseEntity<SubjectModel> addInterestedUserByComponentID(@RequestHeader(USER_HEADER_TOKEN_NAME) String credential, @RequestParam String componentID) {
        return ResponseEntity.ok().body(service.addInterestedUserByComponentID(credential, componentID));
    }

    @JsonView(Views.Public.class)
    @DeleteMapping("/interested")
    public ResponseEntity<SubjectModel> removeInterestedUserByComponentID(@RequestHeader(USER_HEADER_TOKEN_NAME) String credential, @RequestParam String componentID) {
        return ResponseEntity.ok().body(service.removeInterestedUserByComponentID(credential, componentID));
    }
    
    @Override
    @GetMapping("/findAll")
    public Page<SubjectModel> findAll(@RequestHeader(value = USER_HEADER_TOKEN_NAME, required = false) String credential, Pageable pageable) {
        Page<SubjectModel> listResult = service.findAllByPage(credential, pageable);
        return listResult;
    }

    @RequestMapping(method = RequestMethod.GET, params = {"userGoogleId"})
    public Page<SubjectModel> findAllByInterestedUserWithGoogleId(@RequestParam String userGoogleId, Pageable pageable) {
        Page<SubjectModel> listResult = service.findAllByInterestedUserWithGoogleId(userGoogleId, pageable);
        return listResult;
    }

    @RequestMapping(method = RequestMethod.GET, params = {"componentID"})
    public ResponseEntity<SubjectModel> findByComponentID(@RequestParam String componentID) {
        return ResponseEntity.ok().body(service.findByComponentID(componentID));
    }

    @RequestMapping(method = RequestMethod.GET, params = {"code"})
    public ResponseEntity<SubjectModel> findByCode(@RequestParam String code) {
        return ResponseEntity.ok().body(service.findByCode(code));
    }

    @RequestMapping(method = RequestMethod.GET, params = {"modality"})
    public ResponseEntity<List<SubjectModel>> findAllByModality(@RequestParam SubjectModalityType modality) {
        return ResponseEntity.ok().body(service.findAllByModality(modality));
    }

    @RequestMapping(method = RequestMethod.GET, params = {"totalHours"})
    public ResponseEntity<List<SubjectModel>> findAllByTotalHours(@RequestParam int totalHours) {
        return ResponseEntity.ok().body(service.findAllByTotalHours(totalHours));
    }

    @RequestMapping(method = RequestMethod.GET, params = {"partialName", "partialDepartment", "partialCode"})
    public Page<SubjectModel> findAllByNameAndDepartment(Pageable pageable, @RequestParam String partialName, @RequestParam String partialDepartment, @RequestParam String partialCode) {
        return service.findAllByNameAndDepartmentAndCode(pageable, partialName, partialDepartment, partialCode);
    }
    
}