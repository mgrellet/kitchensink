package com.gl.kitchensink.service;

import com.gl.kitchensink.entity.Member;
import com.gl.kitchensink.repository.MemberRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

  MemberRepository memberRepository;

  public MemberService(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  /**
   * Creates a new member from the values provided. Performs validation and throws validation
   * exceptions based on the type of error.
   *
   * @param member The member to register
   * @return The created member
   */
  public Member register(Member member) {
    validateMember(member);
    return memberRepository.save(member);
  }

  /**
   * Find member by id
   *
   * @param id The id of the member
   * @return the member
   */
  public Member findById(Long id) {
    return memberRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Member not found"));
  }

  /**
   * Returns all the members registered
   *
   * @return All members
   */
  public List<Member> findAll() {
    return memberRepository.findAll();
  }

  /**
   * <p>
   * Validates the given Member variable and throws validation exceptions based on the type of
   * error. If the error is standard bean validation errors then it will throw a
   * ConstraintValidationException with the set of the constraints violated.
   * </p>
   * <p>
   * If the error is caused because an existing member with the same email is registered it throws a
   * regular validation exception so that it can be interpreted separately.
   * </p>
   *
   * @param member Member to be validated
   * @throws ConstraintViolationException If Bean Validation errors exist
   * @throws ValidationException          If member with the same email already exists
   */
  private void validateMember(Member member) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    Set<ConstraintViolation<Member>> violations = validator.validate(member);

    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(new HashSet<>(violations));
    }

    if (emailAlreadyExists(member.getEmail())) {
      throw new ValidationException("Unique Email Violation");
    }
  }

  /**
   * Checks if a member with the same email address is already registered.
   *
   * @param email The email to check
   * @return True if the email already exists, and false otherwise
   */
  private boolean emailAlreadyExists(String email) {
    List<Member> existingMembers = findAll();
    return existingMembers
        .stream()
        .anyMatch(m -> m.getEmail().equals(email));
  }
}
