/*
 * Copyright (c) 2002-2026, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.wiki.modules.quiz.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.wiki.business.item.AbstractWikiItem;
import fr.paris.lutece.plugins.wiki.business.item.impl.Book;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuestionType;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.Quiz;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizAnswer;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizAttempt;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizQuestion;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizQuestionHome;
import fr.paris.lutece.plugins.wiki.modules.quiz.service.QuizService;
import fr.paris.lutece.plugins.wiki.service.WikiItemService;
import fr.paris.lutece.plugins.wiki.service.security.WikiAccessControlService;
import fr.paris.lutece.plugins.wiki.web.AbstractWikiXPage;
import fr.paris.lutece.portal.service.security.ISecurityTokenService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.portal.web.xpages.XPage;

/**
 * XPage controller for quiz viewing and participation. Handles quiz listing, playing, results viewing, and certificate access.
 */
@RequestScoped
@Named( "wiki-quiz.xpage.quiz" )
@Controller( xpageName = "quiz", pageTitleI18nKey = "module.wiki.quiz.xpage.quiz.pageTitle", pagePathI18nKey = "module.wiki.quiz.xpage.quiz.pageTitle" )
public class QuizXPage extends AbstractWikiXPage
{
    private static final long serialVersionUID = 1L;

    @Inject
    private ISecurityTokenService _securityTokenService;

    @Inject
    private Models _models;

    private static final String VIEW_QUIZ_LIST = "quizList";
    private static final String VIEW_QUIZ_DETAIL = "quizDetail";
    private static final String VIEW_PLAY_QUIZ = "playQuiz";
    private static final String VIEW_QUIZ_RESULTS = "quizResults";
    private static final String VIEW_QUIZ_HISTORY = "quizHistory";

    private static final String ACTION_START_ATTEMPT = "startAttempt";
    private static final String ACTION_SUBMIT_QUIZ = "submitQuiz";
    private static final String ACTION_DELETE_QUIZ = "deleteQuiz";

    private static final String TEMPLATE_QUIZ_LIST = "skin/plugins/wiki/modules/quiz/quiz_list.html";
    private static final String TEMPLATE_QUIZ_DETAIL = "skin/plugins/wiki/modules/quiz/quiz_detail.html";
    private static final String TEMPLATE_PLAY_QUIZ = "skin/plugins/wiki/modules/quiz/quiz_play.html";
    private static final String TEMPLATE_QUIZ_RESULTS = "skin/plugins/wiki/modules/quiz/quiz_results.html";
    private static final String TEMPLATE_QUIZ_HISTORY = "skin/plugins/wiki/modules/quiz/quiz_history.html";

    private static final String PARAMETER_BOOK_ID = "book_id";
    private static final String PARAMETER_QUIZ_ID = "quiz_id";
    private static final String PARAMETER_ATTEMPT_ID = "attempt_id";
    private static final String PARAMETER_ANSWER_PREFIX = "answer_";
    private static final String PARAMETER_FROM = "from";

    private static final String MARK_QUIZZES = "quizzes";
    private static final String MARK_QUIZ = "quiz";
    private static final String MARK_QUESTIONS = "questions";
    private static final String MARK_ATTEMPT = "attempt";
    private static final String MARK_ATTEMPTS = "attempts";
    private static final String MARK_CAN_START = "can_start";
    private static final String MARK_PAGES_MAP = "pages_map";
    private static final String MARK_FROM = "from";
    private static final String MARK_QUIZ_ID = "quiz_id";

    private static final String URL_QUIZ_LIST = "Portal.jsp?page=quiz&view=quizList&book_id=";
    private static final String URL_WIKI_HOME = "Portal.jsp?page=wiki";

    /**
     * Displays the list of quizzes for a book.
     *
     * @param request
     *            the HTTP servlet request
     * @return the quiz list XPage
     */
    @View( value = VIEW_QUIZ_LIST, defaultView = true )
    public XPage viewQuizList( HttpServletRequest request )
    {
        String strBookId = request.getParameter( PARAMETER_BOOK_ID );

        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

        if ( strBookId != null && !strBookId.isEmpty( ) )
        {
            int nBookId = Integer.parseInt( strBookId );
            AbstractWikiItem item = WikiItemService.findById( nBookId );

            if ( item instanceof Book )
            {
                Book book = (Book) item;
                if ( WikiAccessControlService.canView( user, book ) )
                {
                    boolean bCanEdit = WikiAccessControlService.canEdit( user, book );
                    List<Quiz> quizzes = bCanEdit ? QuizService.getQuizzesByBook( nBookId ) : QuizService.getPublishedQuizzesByBook( nBookId );
                    _models.put( MARK_QUIZZES, quizzes );
                    populateBookSidebarModel( _models, user, book );
                }
            }
        }
        else
        {
            populateCommonModel( _models, user );
        }

        return getXPage( TEMPLATE_QUIZ_LIST, getLocale( request ) );
    }

    /**
     * Displays the detail view of a quiz.
     *
     * @param request
     *            the HTTP servlet request
     * @return the quiz detail XPage
     */
    @View( VIEW_QUIZ_DETAIL )
    public XPage viewQuizDetail( HttpServletRequest request )
    {
        String strQuizId = request.getParameter( PARAMETER_QUIZ_ID );

        if ( strQuizId == null || strQuizId.isEmpty( ) )
        {
            addError( "module.wiki.quiz.quiz.error.quizRequired", getLocale( request ) );
            return redirect( request, URL_WIKI_HOME );
        }

        int nQuizId = Integer.parseInt( strQuizId );
        Quiz quiz = QuizService.findById( nQuizId );

        if ( quiz == null )
        {
            addError( "module.wiki.quiz.quiz.error.quizNotFound", getLocale( request ) );
            return redirect( request, URL_WIKI_HOME );
        }

        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

        AbstractWikiItem bookItem = WikiItemService.findById( quiz.getIdBook( ) );
        if ( !( bookItem instanceof Book ) || !WikiAccessControlService.canView( user, bookItem ) )
        {
            addError( "module.wiki.quiz.quiz.error.accessDenied", getLocale( request ) );
            return redirect( request, URL_QUIZ_LIST + quiz.getIdBook( ) );
        }

        Book book = (Book) bookItem;
        boolean bCanEdit = WikiAccessControlService.canEdit( user, book );

        if ( !quiz.isPublished( ) && !bCanEdit )
        {
            addError( "module.wiki.quiz.quiz.error.quizNotPublished", getLocale( request ) );
            return redirect( request, URL_QUIZ_LIST + book.getId( ) );
        }

        if ( !quiz.isPublished( ) && bCanEdit )
        {
            addWarning( "module.wiki.quiz.quiz.warning.quizNotPublished", getLocale( request ) );
        }

        _models.put( MARK_QUIZ, quiz );
        _models.put( MARK_CAN_EDIT, bCanEdit );
        populateBookSidebarModel( _models, user, book );

        if ( user != null )
        {
            boolean bCanStart = QuizService.canStartAttempt( quiz, user.getName( ), bCanEdit );
            _models.put( MARK_CAN_START, bCanStart );

            List<QuizAttempt> attempts = QuizService.getUserAttempts( nQuizId, user.getName( ) );
            _models.put( MARK_ATTEMPTS, attempts );

            QuizAttempt inProgressAttempt = QuizService.getInProgressAttempt( nQuizId, user.getName( ) );
            if ( inProgressAttempt != null )
            {
                _models.put( MARK_ATTEMPT, inProgressAttempt );
            }
        }

        _models.put( SecurityTokenService.MARK_TOKEN, _securityTokenService.getToken( request, ACTION_START_ATTEMPT ) );
        if ( bCanEdit )
        {
            _models.put( "delete_token", _securityTokenService.getToken( request, ACTION_DELETE_QUIZ ) );
        }

        return getXPage( TEMPLATE_QUIZ_DETAIL, getLocale( request ) );
    }

    /**
     * Starts a new quiz attempt for the current user.
     *
     * @param request
     *            the HTTP servlet request
     * @return redirect to play quiz view
     * @throws UserNotSignedException
     *             if user is not authenticated
     */
    @Action( ACTION_START_ATTEMPT )
    public XPage doStartAttempt( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = checkAuthentication( request );

        String strQuizId = request.getParameter( PARAMETER_QUIZ_ID );
        int nQuizId = Integer.parseInt( strQuizId );

        Quiz quiz = QuizService.findById( nQuizId );
        if ( quiz == null )
        {
            return redirect( request, URL_WIKI_HOME );
        }

        AbstractWikiItem bookItem = WikiItemService.findById( quiz.getIdBook( ) );
        boolean bCanEdit = ( bookItem instanceof Book ) && WikiAccessControlService.canEdit( user, bookItem );

        if ( !quiz.isPublished( ) && !bCanEdit )
        {
            addError( "module.wiki.quiz.quiz.error.quizNotPublished", getLocale( request ) );
            return redirect( request, URL_QUIZ_LIST + quiz.getIdBook( ) );
        }

        if ( !QuizService.canStartAttempt( quiz, user.getName( ), bCanEdit ) )
        {
            addError( "module.wiki.quiz.quiz.error.maxAttemptsReached", getLocale( request ) );
            Map<String, String> params = new HashMap<>( );
            params.put( PARAMETER_QUIZ_ID, strQuizId );
            return redirect( request, VIEW_QUIZ_DETAIL, params );
        }

        QuizAttempt inProgressAttempt = QuizService.getInProgressAttempt( nQuizId, user.getName( ) );
        if ( inProgressAttempt != null )
        {
            Map<String, String> params = new HashMap<>( );
            params.put( PARAMETER_ATTEMPT_ID, String.valueOf( inProgressAttempt.getId( ) ) );
            return redirect( request, VIEW_PLAY_QUIZ, params );
        }

        QuizAttempt attempt = QuizService.startAttempt( nQuizId, user );

        Map<String, String> params = new HashMap<>( );
        params.put( PARAMETER_ATTEMPT_ID, String.valueOf( attempt.getId( ) ) );
        return redirect( request, VIEW_PLAY_QUIZ, params );
    }

    /**
     * Displays the quiz play view for an attempt.
     *
     * @param request
     *            the HTTP servlet request
     * @return the play quiz XPage
     * @throws UserNotSignedException
     *             if user is not authenticated
     */
    @View( VIEW_PLAY_QUIZ )
    public XPage viewPlayQuiz( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = checkAuthentication( request );

        String strAttemptId = request.getParameter( PARAMETER_ATTEMPT_ID );
        if ( strAttemptId == null || strAttemptId.isEmpty( ) )
        {
            return redirectView( request, VIEW_QUIZ_LIST );
        }

        int nAttemptId = Integer.parseInt( strAttemptId );
        QuizAttempt attempt = QuizService.getAttemptWithResponses( nAttemptId );

        if ( attempt == null || !attempt.getUserGuid( ).equals( user.getName( ) ) )
        {
            return redirectView( request, VIEW_QUIZ_LIST );
        }

        if ( attempt.isCompleted( ) )
        {
            Map<String, String> params = new HashMap<>( );
            params.put( PARAMETER_ATTEMPT_ID, strAttemptId );
            return redirect( request, VIEW_QUIZ_RESULTS, params );
        }

        Quiz quiz = QuizService.findById( attempt.getIdQuiz( ) );

        long lTimeRemainingSeconds = QuizService.calculateTimeRemainingSeconds( attempt, quiz );
        if ( lTimeRemainingSeconds == 0 )
        {
            QuizService.completeAttempt( nAttemptId );
            addError( "module.wiki.quiz.quiz.error.timeExpired", getLocale( request ) );
            Map<String, String> params = new HashMap<>( );
            params.put( PARAMETER_ATTEMPT_ID, strAttemptId );
            return redirect( request, VIEW_QUIZ_RESULTS, params );
        }

        List<QuizQuestion> questions = QuizService.getQuestionsForAttempt( quiz );

        _models.put( MARK_QUIZ, quiz );
        _models.put( MARK_ATTEMPT, attempt );
        _models.put( MARK_QUESTIONS, questions );
        _models.put( MARK_USER, user );
        _models.put( "timeRemainingSeconds", lTimeRemainingSeconds );
        _models.put( SecurityTokenService.MARK_TOKEN, _securityTokenService.getToken( request, ACTION_SUBMIT_QUIZ ) );

        return getXPage( TEMPLATE_PLAY_QUIZ, getLocale( request ) );
    }

    /**
     * Submits quiz answers and completes the attempt.
     *
     * @param request
     *            the HTTP servlet request
     * @return redirect to quiz results view
     * @throws UserNotSignedException
     *             if user is not authenticated
     */
    @Action( ACTION_SUBMIT_QUIZ )
    public XPage doSubmitQuiz( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = checkAuthentication( request );

        String strAttemptId = request.getParameter( PARAMETER_ATTEMPT_ID );
        int nAttemptId = Integer.parseInt( strAttemptId );

        QuizAttempt attempt = QuizService.getAttemptWithResponses( nAttemptId );
        if ( attempt == null || !attempt.getUserGuid( ).equals( user.getName( ) ) )
        {
            return redirectView( request, VIEW_QUIZ_LIST );
        }

        Quiz quiz = QuizService.getQuizWithQuestions( attempt.getIdQuiz( ) );

        if ( QuizService.isTimeExpired( attempt, quiz ) )
        {
            QuizService.completeAttempt( nAttemptId );
            addError( "module.wiki.quiz.quiz.error.timeExpired", getLocale( request ) );
            Map<String, String> params = new HashMap<>( );
            params.put( PARAMETER_ATTEMPT_ID, strAttemptId );
            return redirect( request, VIEW_QUIZ_RESULTS, params );
        }

        for ( QuizQuestion question : quiz.getQuestions( ) )
        {
            String strAnswer = null;

            if ( question.getQuestionType( ) == QuestionType.MATCHING )
            {
                strAnswer = buildMatchingAnswer( request, question );
            }
            else
            {
                strAnswer = request.getParameter( PARAMETER_ANSWER_PREFIX + question.getId( ) );
            }

            if ( strAnswer != null && !strAnswer.isEmpty( ) )
            {
                QuizService.submitAnswer( nAttemptId, question.getId( ), strAnswer );
            }
        }

        QuizService.completeAttempt( nAttemptId );

        Map<String, String> params = new HashMap<>( );
        params.put( PARAMETER_ATTEMPT_ID, strAttemptId );
        return redirect( request, VIEW_QUIZ_RESULTS, params );
    }

    /**
     * Builds a JSON answer string for matching questions.
     *
     * @param request
     *            the HTTP servlet request
     * @param question
     *            the quiz question
     * @return JSON string of matching answers
     */
    private String buildMatchingAnswer( HttpServletRequest request, QuizQuestion question )
    {
        String strPrefix = "match_" + question.getId( ) + "_";
        StringBuilder sbJson = new StringBuilder( "{" );
        boolean bFirst = true;

        for ( QuizAnswer answer : question.getAnswers( ) )
        {
            String strValue = request.getParameter( strPrefix + answer.getId( ) );
            if ( strValue != null && !strValue.isEmpty( ) )
            {
                if ( !bFirst )
                {
                    sbJson.append( "," );
                }
                sbJson.append( "\"" ).append( answer.getId( ) ).append( "\":\"" ).append( escapeJson( strValue ) ).append( "\"" );
                bFirst = false;
            }
        }

        sbJson.append( "}" );
        return bFirst ? null : sbJson.toString( );
    }

    /**
     * Escapes special characters for JSON string.
     *
     * @param str
     *            the string to escape
     * @return escaped string
     */
    private String escapeJson( String str )
    {
        if ( str == null )
        {
            return "";
        }
        return str.replace( "\\", "\\\\" )
                  .replace( "\"", "\\\"" )
                  .replace( "\n", "\\n" )
                  .replace( "\r", "\\r" )
                  .replace( "\t", "\\t" );
    }

    /**
     * Displays the quiz results for a completed attempt.
     *
     * @param request
     *            the HTTP servlet request
     * @return the quiz results XPage
     * @throws UserNotSignedException
     *             if user is not authenticated
     */
    @View( VIEW_QUIZ_RESULTS )
    public XPage viewQuizResults( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = checkAuthentication( request );

        String strAttemptId = request.getParameter( PARAMETER_ATTEMPT_ID );
        if ( strAttemptId == null || strAttemptId.isEmpty( ) )
        {
            return redirectView( request, VIEW_QUIZ_LIST );
        }

        int nAttemptId = Integer.parseInt( strAttemptId );
        QuizAttempt attempt = QuizService.getAttemptWithResponses( nAttemptId );

        if ( attempt == null )
        {
            return redirectView( request, VIEW_QUIZ_LIST );
        }

        Quiz quiz = QuizService.getQuizWithQuestions( attempt.getIdQuiz( ) );

        AbstractWikiItem bookItem = WikiItemService.findById( quiz.getIdBook( ) );
        Book book = ( bookItem instanceof Book ) ? (Book) bookItem : null;

        boolean bIsOwner = attempt.getUserGuid( ).equals( user.getName( ) );
        boolean bCanEdit = book != null && WikiAccessControlService.canEdit( user, book );

        if ( !bIsOwner && !bCanEdit )
        {
            return redirectView( request, VIEW_QUIZ_LIST );
        }

        Map<String, AbstractWikiItem> pagesMap = new HashMap<>( );
        for ( QuizQuestion question : quiz.getQuestions( ) )
        {
            List<Integer> sourcePageIds = QuizQuestionHome.getSourcePageIds( question.getId( ) );
            question.setSourcePageIds( sourcePageIds );
            for ( Integer pageId : sourcePageIds )
            {
                String strPageId = String.valueOf( pageId );
                if ( !pagesMap.containsKey( strPageId ) )
                {
                    AbstractWikiItem page = WikiItemService.findById( pageId );
                    if ( page != null )
                    {
                        pagesMap.put( strPageId, page );
                    }
                }
            }
        }

        _models.put( MARK_QUIZ, quiz );
        _models.put( MARK_ATTEMPT, attempt );
        _models.put( MARK_QUESTIONS, quiz.getQuestions( ) );
        _models.put( MARK_USER, user );
        _models.put( MARK_PAGES_MAP, pagesMap );
        _models.put( MARK_FROM, request.getParameter( PARAMETER_FROM ) );
        _models.put( MARK_QUIZ_ID, request.getParameter( PARAMETER_QUIZ_ID ) );

        if ( book != null )
        {
            populateBookSidebarModel( _models, user, book );
        }

        return getXPage( TEMPLATE_QUIZ_RESULTS, getLocale( request ) );
    }

    /**
     * Displays the quiz history for a user.
     *
     * @param request
     *            the HTTP servlet request
     * @return the quiz history XPage
     * @throws UserNotSignedException
     *             if user is not authenticated
     */
    @View( VIEW_QUIZ_HISTORY )
    public XPage viewQuizHistory( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = checkAuthentication( request );

        String strQuizId = request.getParameter( PARAMETER_QUIZ_ID );
        if ( strQuizId == null || strQuizId.isEmpty( ) )
        {
            return redirectView( request, VIEW_QUIZ_LIST );
        }

        int nQuizId = Integer.parseInt( strQuizId );
        Quiz quiz = QuizService.findById( nQuizId );

        if ( quiz == null )
        {
            return redirectView( request, VIEW_QUIZ_LIST );
        }

        List<QuizAttempt> attempts = QuizService.getUserAttempts( nQuizId, user.getName( ) );

        _models.put( MARK_QUIZ, quiz );
        _models.put( MARK_ATTEMPTS, attempts );
        _models.put( MARK_USER, user );

        return getXPage( TEMPLATE_QUIZ_HISTORY, getLocale( request ) );
    }

    /**
     * Checks if the user is authenticated.
     *
     * @param request
     *            the HTTP servlet request
     * @return the authenticated user
     * @throws UserNotSignedException
     *             if user is not authenticated
     */
    private LuteceUser checkAuthentication( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
        if ( user == null )
        {
            throw new UserNotSignedException( );
        }
        return user;
    }
}
