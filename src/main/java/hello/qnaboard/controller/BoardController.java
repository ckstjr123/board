package hello.qnaboard.controller;

import hello.qnaboard.argumentresolver.Login;
import hello.qnaboard.constant.BoardType;
import hello.qnaboard.repository.dto.BoardSearchCond;
import hello.qnaboard.repository.dto.BoardUpdateForm;
import hello.qnaboard.repository.vo.BoardListItem;
import hello.qnaboard.repository.vo.BoardVO;
import hello.qnaboard.repository.vo.CommentVO;
import hello.qnaboard.service.BoardService;
import hello.qnaboard.service.BoardWithCommentsVO;
import hello.qnaboard.service.BoardWriteForm;
import hello.qnaboard.vo.pagination.Page;
import hello.qnaboard.vo.pagination.PageRequest;
import hello.qnaboard.vo.pagination.Pageable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @ResponseBody
    @GetMapping("/hot")
    public String hot() {
//        request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI();
        return "hotArticles";
    }

    /**
     * 해당 게시판 페이지(게시글 리스트, 로그인 체크 X)
     */
    @GetMapping("/{boardType}")
    public String board(@PathVariable BoardType boardType, @RequestParam(required = false) Integer category, @ModelAttribute BoardSearchCond boardSearchCond,
                        @RequestParam(defaultValue = "1") int page, @Login SessionMember loginMember, Model model) {
//        model.addAttribute("boardType", boardType); (ThymeleafView는 스프링 @PathVariable uri 변수를 자동으로 모델에 담음)

        Pageable pageable = PageRequest.of(page, 10); // 페이지당 게시글 항목 최대 10개
        Page<BoardListItem> boardListPage = this.boardService.boardListPage(boardType, pageable, boardSearchCond);

        model.addAttribute("boardListPage", boardListPage);
        model.addAttribute("loginMember", loginMember);

        //게시판 쿼리파라미터 빌드
        String boardListParams = QueryParamBuilder.newInstance()
                .queryParam("category", category)
                .queryParam("searchTarget", boardSearchCond.getSearchTarget())
                .queryParam("searchKeyword", boardSearchCond.getSearchKeyword())
                .toQueryParamString();

        model.addAttribute("bdListParams", !boardListParams.isEmpty() ? ("?" + boardListParams) : ""); // 게시판 리스트 관련 쿼리파라미터가 존재하면 모델에 담음
        model.addAttribute("category", category);
        model.addAttribute("page", page);
        return "board/boardList";
    }

    /**
     * 게시물 상세(로그인 체크 X)
     */
    @GetMapping("/{boardType}/{boardId}")
    public String boardDetail(@PathVariable BoardType boardType, @PathVariable Long boardId, @Login SessionMember loginMember,
                              HttpServletRequest request, Model model) {
        BoardWithCommentsVO boardWithCommentsVO = this.boardService.boardDetail(boardId); // 해당 게시물 조회

        BoardVO boardVO = boardWithCommentsVO.getBoardVO();
        List<CommentVO> commentVoList = boardWithCommentsVO.getCommentVoList();

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("board", boardVO);
        model.addAttribute("boardUrl", request.getRequestURL()); // 게시물 url
        model.addAttribute("commentList", commentVoList);
        return "board/boardDetail";
    }
    
    /**
     * 게시물 작성 폼
     */
    @GetMapping("/{boardType}/write")
    public String boardWriteForm(@PathVariable BoardType boardType, @Login SessionMember loginMember, Model model) {
        model.addAttribute("boardWriteForm", new BoardWriteForm());
        model.addAttribute("loginMember", loginMember);
        return "board/boardWriteForm"; // 글쓰기 페이지
    }

    /**
     * 게시물 등록
     */
    @PostMapping("/{boardType}/write")
    public String writeBoard(@PathVariable BoardType boardType, @ModelAttribute @Valid BoardWriteForm boardWriteForm, BindingResult bindingResult,
                             @Login SessionMember loginMember, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("loginMember", loginMember);
            return "board/boardWriteForm";
        }

        this.boardService.post(boardType, boardWriteForm, loginMember.getMemberId());
        return "redirect:/{boardType}";
    }

    /**
     * 게시물 수정 폼
     */
    @GetMapping("/{boardType}/{boardId}/edit")
    public String boardEditForm(@PathVariable BoardType boardType, @PathVariable Long boardId, @Login SessionMember loginMember, Model model) {
        BoardVO boardVO = this.boardService.validateBoard(boardId, loginMember.getMemberId());
        BoardUpdateForm boardUpdateForm = BoardUpdateForm.of(boardVO);

        model.addAttribute("boardId", boardId);
        model.addAttribute("boardUpdateForm", boardUpdateForm);
        model.addAttribute("loginMember", loginMember);
        return "board/boardEditForm";
    }

    /**
     * 게시물 수정
     */
    @PostMapping("/{boardType}/{boardId}/edit")
    public String editBoard(@PathVariable BoardType boardType, @PathVariable Long boardId, @ModelAttribute("boardUpdateForm") @Valid BoardUpdateForm updateParam,
                            BindingResult bindingResult, @Login SessionMember loginMember, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("loginMember", loginMember);
            return "board/boardEditForm";
        }

        this.boardService.edit(boardId, updateParam, loginMember.getMemberId());
        return "redirect:/{boardType}";
    }

}
