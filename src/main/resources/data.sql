SELECT 1;
insert into authority (authority_name, created_at, last_modified_at, created_by, last_modified_by) values
                                                                                                       ('ROLE_GUEST', NOW(), NOW(), 'ddooby', 'ddooby'),
                                                                                                       ('ROLE_USER', NOW(), NOW(), 'ddooby', 'ddooby'),
                                                                                                       ('ROLE_ADMIN', NOW(), NOW(), 'ddooby', 'ddooby');
