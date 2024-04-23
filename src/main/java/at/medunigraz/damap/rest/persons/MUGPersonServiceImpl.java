package at.medunigraz.damap.rest.persons;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import at.ac.tuwien.damap.rest.base.ResultList;
import at.ac.tuwien.damap.rest.base.Search;
import at.ac.tuwien.damap.rest.dmp.domain.ContributorDO;
import at.ac.tuwien.damap.rest.persons.PersonService;
import at.medunigraz.api.rest.base.models.MUGSearchResult;
import at.medunigraz.damap.rest.dmp.domain.MUGPerson;
import at.medunigraz.damap.rest.dmp.mapper.MUGPersonDOMapper;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@ApplicationScoped
public class MUGPersonServiceImpl implements PersonService {

    @Inject
    @RestClient
    MUGPersonRestService restService;

    @Override
    public ContributorDO read(String id, MultivaluedMap<String, String> queryParams) {
        MUGPerson contributor = restService.read(id);

        return MUGPersonDOMapper.mapEntityToDO(contributor, new ContributorDO());
    }

    @Override
    public ResultList<ContributorDO> search(MultivaluedMap<String, String> queryParams) {
        Search s = Search.fromMap(queryParams);
        int limit = s.getPagination().getPerPage();
        int offset = ((s.getPagination().getPage() < 1 ? 1 : s.getPagination().getPage()) - 1) * limit;

        MUGSearchResult<MUGPerson> people = restService.search(s.getQuery(), offset, limit);

        List<ContributorDO> contributors = people.getResults().stream()
                .map(c -> MUGPersonDOMapper.mapEntityToDO(c, new ContributorDO()))
                .collect(Collectors.toList());

        s.getPagination().setNumTotalItems(people.getCount());
        return ResultList.fromItemsAndSearch(contributors, s);
    }
}