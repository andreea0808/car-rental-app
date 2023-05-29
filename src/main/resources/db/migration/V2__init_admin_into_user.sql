insert into t_user (id, email, firstname,  lastname, password, role)
values (
           nextVal('t_user_id_seq'),
           'andreea.oprea0808@gmail.com',
           'Andreea',
           'Oprea',
            '$2a$12$AesOU7WctVFnLLMhIt5cS.tMgnjdKBpT6nMSvJK3nnWqZB25wO9KO',
           'ADMIN'
       );