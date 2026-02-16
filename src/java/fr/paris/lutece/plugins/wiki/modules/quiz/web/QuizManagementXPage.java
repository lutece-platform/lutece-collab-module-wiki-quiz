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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.wiki.business.item.AbstractWikiItem;
import fr.paris.lutece.plugins.wiki.business.item.WikiItemHome;
import fr.paris.lutece.plugins.wiki.business.item.impl.Book;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuestionType;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.Quiz;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizAnswer;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizAnswerHome;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizQuestion;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizQuestionHome;
import fr.paris.lutece.plugins.wiki.exception.WikiValidationException;
import fr.paris.lutece.plugins.wiki.modules.quiz.service.QuizService;
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
import fr.paris.lutece.util.bean.BeanUtil;

/**
 * XPage controller for quiz management operations. Handles CRUD operations for quizzes and quiz questions.
 */
@RequestScoped
@Named( "wiki-quiz.xpage.quizmanagement" )
@Controller( xpageName = "quizmanagement", pageTitleI18nKey = "module.wiki.quiz.xpage.quizManagement.pageTitle", pagePathI18nKey = "module.wiki.quiz.xpage.quizManagement.pageTitle" )
public class QuizManagementXPage extends AbstractWikiXPage
{
    private static final long serialVersionUID = 1L;

    @Inject
    private ISecurityTokenService _securityTokenService;

    @Inject
    private Models _models;

    private static final String VIEW_CREATE_QUIZ = "createQuiz";
    private static final String VIEW_MODIFY_QUIZ = "modifyQuiz";
    private static final String VIEW_MANAGE_QUESTIONS = "manageQuestions";
    private static final String VIEW_CREATE_QUESTION = "createQuestion";
    private static final String VIEW_MODIFY_QUESTION = "modifyQuestion";

    private static final String ACTION_CREATE_QUIZ = "createQuiz";
    private static final String ACTION_UPDATE_QUIZ = "updateQuiz";
    private static final String ACTION_DELETE_QUIZ = "deleteQuiz";
    private static final String ACTION_CREATE_QUESTION = "createQuestion";
    private static final String ACTION_UPDATE_QUESTION = "updateQuestion";
    private static final String ACTION_DELETE_QUESTION = "deleteQuestion";
    private static final String ACTION_REORDER_QUESTIONS = "reorderQuestions";

    private static final String TEMPLATE_CREATE_QUIZ = "skin/plugins/wiki/modules/quiz/manage_quiz_form.html";
    private static final String TEMPLATE_MODIFY_QUIZ = "skin/plugins/wiki/modules/quiz/manage_quiz_form.html";
    private static final String TEMPLATE_MANAGE_QUESTIONS = "skin/plugins/wiki/modules/quiz/manage_questions.html";
    private static final String TEMPLATE_QUESTION_FORM = "skin/plugins/wiki/modules/quiz/manage_question_form.html";

    private static final String PARAMETER_BOOK_ID = "book_id";
    private static final String PARAMETER_QUIZ_ID = "quiz_id";
    private static final String PARAMETER_QUESTION_ID = "question_id";
    private static final String PARAMETER_ANSWER_TEXT = "answer_text_";
    private static final String PARAMETER_ANSWER_CORRECT = "answer_correct_";
    private static final String PARAMETER_ANSWER_MATCH = "answer_match_";
    private static final String PARAMETER_ANSWER_ORDER = "answer_order_";
    private static final String PARAMETER_ANSWER_COUNT = "answer_count";
    private static final String PARAMETER_ANSWER_CORRECT_RADIO = "answer_correct";
    private static final String PARAMETER_QUESTION_TYPE_CODE = "questionTypeCode";

    private static final int MAX_ANSWER_COUNT = 100;

    private static final String MARK_QUIZ = "quiz";
    private static final String MARK_QUESTIONS = "questions";
    private static final String MARK_QUESTION = "question";
    private static final String MARK_QUESTION_TYPES = "question_types";
    private static final String MARK_IS_EDIT = "is_edit";
    private static final String PARAMETER_QUESTION_PAGES = "question_pages";
    private static final String URL_QUIZ_LIST = "Portal.jsp?page=quiz&view=quizList&book_id=";
    private static final String URL_QUIZ_DETAIL = "Portal.jsp?page=quiz&view=quizDetail&quiz_id=";
    private static final String URL_WIKI_HOME = "Portal.jsp?page=wiki";

    /**
     * Displays the quiz creation form.
     *
     * @param request
     *            the HTTP servlet request
     * @return the create quiz XPage
     * @throws UserNotSignedException
     *             if user is not authenticated
     */
    @View( value = VIEW_CREATE_QUIZ, defaultView = true )
    public XPage viewCreateQuiz( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = checkAuthentication( request );

        String strBookId = request.getParameter( PARAMETER_BOOK_ID );
        if ( strBookId == null || strBookId.isEmpty( ) )
        {
            return redirect( request, URL_WIKI_HOME );
        }

        int nBookId = Integer.parseInt( strBookId );
        Optional<AbstractWikiItem> optBook = WikiItemHome.findByPrimaryKey( nBookId );

        if ( optBook.isEmpty( ) || !WikiAccessControlService.canEdit( user, optBook.get( ) ) )
        {
            return redirectToQuizList( request, nBookId );
        }

        Quiz quiz = new Quiz( );
        quiz.setIdBook( nBookId );

        _models.put( MARK_QUIZ, quiz );
        _models.put( MARK_BOOK, optBook.get( ) );
        _models.put( MARK_IS_EDIT, false );
        _models.put( SecurityTokenService.MARK_TOKEN, _securityTokenService.getToken( request, ACTION_CREATE_QUIZ ) );

        return getXPage( TEMPLATE_CREATE_QUIZ, getLocale( request ) );
    }

    /**
     * Processes quiz creation.
     *
     * @param request
     *            the HTTP servlet request
     * @return redirect to quiz list or back to form on error
     * @throws UserNotSignedException
     *             if user is not authenticated
     */
    @Action( ACTION_CREATE_QUIZ )
    public XPage doCreateQuiz( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = checkAuthentication( request );

        Quiz quiz = new Quiz( );
        BeanUtil.populate( quiz, request );

        Optional<AbstractWikiItem> optBook = WikiItemHome.findByPrimaryKey( quiz.getIdBook( ) );
        if ( optBook.isEmpty( ) || !WikiAccessControlService.canEdit( user, optBook.get( ) ) )
        {
            return redirectToQuizList( request, quiz.getIdBook( ) );
        }

        try
        {
            QuizService.create( quiz, getLocale( request ) );
            addInfo( "module.wiki.quiz.message.quizCreated", getLocale( request ) );
            return redirectToQuizList( request, quiz.getIdBook( ) );
        }
        catch( WikiValidationException e )
        {
            addError( e.getMessage( ) );

            _models.put( MARK_QUIZ, quiz );
            _models.put( MARK_BOOK, optBook.get( ) );
            _models.put( MARK_IS_EDIT, false );
            _models.put( SecurityTokenService.MARK_TOKEN, _securityTokenService.getToken( request, ACTION_CREATE_QUIZ ) );

            return getXPage( TEMPLATE_CREATE_QUIZ, getLocale( request ) );
        }
    }

    /**
     * Displays the quiz modification form.
     *
     * @param request
     *            the HTTP servlet request
     * @return the modify quiz XPage
     * @throws UserNotSignedException
     *             if user is not authenticated
     */
    @View( VIEW_MODIFY_QUIZ )
    public XPage viewModifyQuiz( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = checkAuthentication( request );

        String strQuizId = request.getParameter( PARAMETER_QUIZ_ID );
        if ( strQuizId == null || strQuizId.isEmpty( ) )
        {
            return redirect( request, URL_WIKI_HOME );
        }

        int nQuizId = Integer.parseInt( strQuizId );
        Quiz quiz = QuizService.findById( nQuizId );

        if ( quiz == null )
        {
            return redirect( request, URL_WIKI_HOME );
        }

        Optional<AbstractWikiItem> optBook = WikiItemHome.findByPrimaryKey( quiz.getIdBook( ) );
        if ( optBook.isEmpty( ) || !WikiAccessControlService.canEdit( user, optBook.get( ) ) )
        {
            return redirectToQuizList( request, quiz.getIdBook( ) );
        }

        _models.put( MARK_QUIZ, quiz );
        _models.put( MARK_BOOK, optBook.get( ) );
        _models.put( MARK_IS_EDIT, true );
        _models.put( SecurityTokenService.MARK_TOKEN, _securityTokenService.getToken( request, ACTION_UPDATE_QUIZ ) );

        return getXPage( TEMPLATE_MODIFY_QUIZ, getLocale( request ) );
    }

    /**
     * Processes quiz update.
     *
     * @param request
     *            the HTTP servlet request
     * @return redirect to quiz detail or back to form on error
     * @throws UserNotSignedException
     *             if user is not authenticated
     */
    @Action( ACTION_UPDATE_QUIZ )
    public XPage doUpdateQuiz( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = checkAuthentication( request );

        String strQuizId = request.getParameter( PARAMETER_QUIZ_ID );
        int nQuizId = Integer.parseInt( strQuizId );

        Quiz quiz = QuizService.findById( nQuizId );
        if ( quiz == null )
        {
            return redirect( request, URL_WIKI_HOME );
        }

        Optional<AbstractWikiItem> optBook = WikiItemHome.findByPrimaryKey( quiz.getIdBook( ) );
        if ( optBook.isEmpty( ) || !WikiAccessControlService.canEdit( user, optBook.get( ) ) )
        {
            return redirectToQuizList( request, quiz.getIdBook( ) );
        }

        BeanUtil.populate( quiz, request );

        try
        {
            QuizService.update( quiz, getLocale( request ) );
            addInfo( "module.wiki.quiz.message.quizUpdated", getLocale( request ) );
            return redirect( request, URL_QUIZ_DETAIL + quiz.getId( ) );
        }
        catch( WikiValidationException e )
        {
            addError( e.getMessage( ) );

            _models.put( MARK_QUIZ, quiz );
            _models.put( MARK_BOOK, optBook.get( ) );
            _models.put( MARK_IS_EDIT, true );
            _models.put( SecurityTokenService.MARK_TOKEN, _securityTokenService.getToken( request, ACTION_UPDATE_QUIZ ) );

            return getXPage( TEMPLATE_MODIFY_QUIZ, getLocale( request ) );
        }
    }

    /**
     * Processes quiz deletion.
     *
     * @param request
     *            the HTTP servlet request
     * @return redirect to quiz list
     * @throws UserNotSignedException
     *             if user is not authenticated
     */
    @Action( ACTION_DELETE_QUIZ )
    public XPage doDeleteQuiz( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = checkAuthentication( request );

        String strQuizId = request.getParameter( PARAMETER_QUIZ_ID );
        int nQuizId = Integer.parseInt( strQuizId );

        Quiz quiz = QuizService.findById( nQuizId );
        if ( quiz == null )
        {
            addError( "module.wiki.quiz.quiz.error.notFound", getLocale( request ) );
            return redirect( request, URL_WIKI_HOME );
        }

        Optional<AbstractWikiItem> optBook = WikiItemHome.findByPrimaryKey( quiz.getIdBook( ) );
        if ( optBook.isEmpty( ) || !WikiAccessControlService.canEdit( user, optBook.get( ) ) )
        {
            addError( "module.wiki.quiz.quiz.error.accessDenied", getLocale( request ) );
            return redirectToQuizList( request, quiz.getIdBook( ) );
        }

        int nBookId = quiz.getIdBook( );
        QuizService.delete( nQuizId );
        addInfo( "module.wiki.quiz.message.quizRemoved", getLocale( request ) );
        return redirectToQuizList( request, nBookId );
    }

    /**
     * Displays the question management view for a quiz.
     *
     * @param request
     *            the HTTP servlet request
     * @return the manage questions XPage
     * @throws UserNotSignedException
     *             if user is not authenticated
     */
    @View( VIEW_MANAGE_QUESTIONS )
    public XPage viewManageQuestions( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = checkAuthentication( request );

        String strQuizId = request.getParameter( PARAMETER_QUIZ_ID );
        if ( strQuizId == null || strQuizId.isEmpty( ) )
        {
            addError( "module.wiki.quiz.quiz.error.quizRequired", getLocale( request ) );
            return redirect( request, URL_WIKI_HOME );
        }

        int nQuizId = Integer.parseInt( strQuizId );
        Quiz quiz = QuizService.getQuizWithQuestions( nQuizId );

        if ( quiz == null )
        {
            addError( "module.wiki.quiz.quiz.error.notFound", getLocale( request ) );
            return redirect( request, URL_WIKI_HOME );
        }

        Optional<AbstractWikiItem> optBook = WikiItemHome.findByPrimaryKey( quiz.getIdBook( ) );
        if ( optBook.isEmpty( ) || !( optBook.get( ) instanceof Book ) || !WikiAccessControlService.canEdit( user, optBook.get( ) ) )
        {
            addError( "module.wiki.quiz.quiz.error.accessDenied", getLocale( request ) );
            return redirectToQuizList( request, quiz.getIdBook( ) );
        }

        Book book = (Book) optBook.get( );
        _models.put( MARK_QUIZ, quiz );
        _models.put( MARK_QUESTIONS, quiz.getQuestions( ) );
        populateBookSidebarModel( _models, user, book );
        _models.put( SecurityTokenService.MARK_TOKEN, _securityTokenService.getToken( request, ACTION_DELETE_QUESTION ) );

        return getXPage( TEMPLATE_MANAGE_QUESTIONS, getLocale( request ) );
    }

    /**
     * Displays the question creation form.
     *
     * @param request
     *            the HTTP servlet request
     * @return the create question XPage
     * @throws UserNotSignedException
     *             if user is not authenticated
     */
    @View( VIEW_CREATE_QUESTION )
    public XPage viewCreateQuestion( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = checkAuthentication( request );

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
            addError( "module.wiki.quiz.quiz.error.notFound", getLocale( request ) );
            return redirect( request, URL_WIKI_HOME );
        }

        Optional<AbstractWikiItem> optBook = WikiItemHome.findByPrimaryKey( quiz.getIdBook( ) );
        if ( optBook.isEmpty( ) || !( optBook.get( ) instanceof Book ) || !WikiAccessControlService.canEdit( user, optBook.get( ) ) )
        {
            addError( "module.wiki.quiz.quiz.error.accessDenied", getLocale( request ) );
            return redirectToQuizList( request, quiz.getIdBook( ) );
        }

        Book book = (Book) optBook.get( );
        QuizQuestion question = new QuizQuestion( );
        question.setIdQuiz( nQuizId );

        _models.put( MARK_QUIZ, quiz );
        _models.put( MARK_QUESTION, question );
        _models.put( MARK_QUESTION_TYPES, QuestionType.values( ) );
        _models.put( MARK_IS_EDIT, false );
        populateBookSidebarModel( _models, user, book );
        _models.put( SecurityTokenService.MARK_TOKEN, _securityTokenService.getToken( request, ACTION_CREATE_QUESTION ) );

        return getXPage( TEMPLATE_QUESTION_FORM, getLocale( request ) );
    }

    /**
     * Processes question creation.
     *
     * @param request
     *            the HTTP servlet request
     * @return redirect to manage questions view
     * @throws UserNotSignedException
     *             if user is not authenticated
     */
    @Action( ACTION_CREATE_QUESTION )
    public XPage doCreateQuestion( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = checkAuthentication( request );

        QuizQuestion question = new QuizQuestion( );
        BeanUtil.populate( question, request );

        Quiz quiz = QuizService.findById( question.getIdQuiz( ) );
        if ( quiz == null )
        {
            addError( "module.wiki.quiz.quiz.error.notFound", getLocale( request ) );
            return redirect( request, URL_WIKI_HOME );
        }

        Optional<AbstractWikiItem> optBook = WikiItemHome.findByPrimaryKey( quiz.getIdBook( ) );
        if ( optBook.isEmpty( ) || !WikiAccessControlService.canEdit( user, optBook.get( ) ) )
        {
            addError( "module.wiki.quiz.quiz.error.accessDenied", getLocale( request ) );
            return redirectToQuizList( request, quiz.getIdBook( ) );
        }

        List<QuizAnswer> answers = extractAnswersFromRequest( request, 0 );
        if ( answers.size( ) < 2 )
        {
            addError( "module.wiki.quiz.quizQuestion.error.minAnswers", getLocale( request ) );
            Book book = (Book) optBook.get( );
            question.setAnswers( answers );

            _models.put( MARK_QUIZ, quiz );
            _models.put( MARK_QUESTION, question );
            _models.put( MARK_QUESTION_TYPES, QuestionType.values( ) );
            _models.put( MARK_IS_EDIT, false );
            populateBookSidebarModel( _models, user, book );
            _models.put( SecurityTokenService.MARK_TOKEN, _securityTokenService.getToken( request, ACTION_CREATE_QUESTION ) );

            return getXPage( TEMPLATE_QUESTION_FORM, getLocale( request ) );
        }

        QuizService.createQuestion( question );

        for ( QuizAnswer answer : answers )
        {
            answer.setIdQuestion( question.getId( ) );
        }
        QuizService.replaceAnswers( question.getId( ), answers );

        String [ ] strQuestionPageIds = request.getParameterValues( PARAMETER_QUESTION_PAGES );
        if ( strQuestionPageIds != null )
        {
            List<Integer> listPageIds = new ArrayList<>( );
            for ( String strPageId : strQuestionPageIds )
            {
                try
                {
                    listPageIds.add( Integer.parseInt( strPageId ) );
                }
                catch( NumberFormatException e )
                {
                }
            }
            QuizQuestionHome.setSourcePages( question.getId( ), listPageIds );
        }

        addInfo( "module.wiki.quiz.message.questionCreated", getLocale( request ) );

        Map<String, String> params = new HashMap<>( );
        params.put( PARAMETER_QUIZ_ID, String.valueOf( question.getIdQuiz( ) ) );
        return redirect( request, VIEW_MANAGE_QUESTIONS, params );
    }

    /**
     * Displays the question modification form.
     *
     * @param request
     *            the HTTP servlet request
     * @return the modify question XPage
     * @throws UserNotSignedException
     *             if user is not authenticated
     */
    @View( VIEW_MODIFY_QUESTION )
    public XPage viewModifyQuestion( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = checkAuthentication( request );

        String strQuestionId = request.getParameter( PARAMETER_QUESTION_ID );
        if ( strQuestionId == null || strQuestionId.isEmpty( ) )
        {
            addError( "module.wiki.quiz.quiz.error.questionRequired", getLocale( request ) );
            return redirect( request, URL_WIKI_HOME );
        }

        int nQuestionId = Integer.parseInt( strQuestionId );
        Optional<QuizQuestion> optQuestion = QuizQuestionHome.findByPrimaryKey( nQuestionId );

        if ( optQuestion.isEmpty( ) )
        {
            addError( "module.wiki.quiz.quiz.error.questionNotFound", getLocale( request ) );
            return redirect( request, URL_WIKI_HOME );
        }

        QuizQuestion question = optQuestion.get( );
        question.setAnswers( QuizAnswerHome.getAnswersByQuestion( nQuestionId ) );
        question.setSourcePageIds( QuizQuestionHome.getSourcePageIds( nQuestionId ) );

        Quiz quiz = QuizService.findById( question.getIdQuiz( ) );
        if ( quiz == null )
        {
            addError( "module.wiki.quiz.quiz.error.notFound", getLocale( request ) );
            return redirect( request, URL_WIKI_HOME );
        }

        Optional<AbstractWikiItem> optBook = WikiItemHome.findByPrimaryKey( quiz.getIdBook( ) );
        if ( optBook.isEmpty( ) || !( optBook.get( ) instanceof Book ) || !WikiAccessControlService.canEdit( user, optBook.get( ) ) )
        {
            addError( "module.wiki.quiz.quiz.error.accessDenied", getLocale( request ) );
            return redirectToQuizList( request, quiz.getIdBook( ) );
        }

        Book book = (Book) optBook.get( );
        _models.put( MARK_QUIZ, quiz );
        _models.put( MARK_QUESTION, question );
        _models.put( MARK_QUESTION_TYPES, QuestionType.values( ) );
        _models.put( MARK_IS_EDIT, true );
        populateBookSidebarModel( _models, user, book );
        _models.put( SecurityTokenService.MARK_TOKEN, _securityTokenService.getToken( request, ACTION_UPDATE_QUESTION ) );

        return getXPage( TEMPLATE_QUESTION_FORM, getLocale( request ) );
    }

    /**
     * Processes question update.
     *
     * @param request
     *            the HTTP servlet request
     * @return redirect to manage questions view
     * @throws UserNotSignedException
     *             if user is not authenticated
     */
    @Action( ACTION_UPDATE_QUESTION )
    public XPage doUpdateQuestion( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = checkAuthentication( request );

        String strQuestionId = request.getParameter( PARAMETER_QUESTION_ID );
        int nQuestionId = Integer.parseInt( strQuestionId );

        Optional<QuizQuestion> optQuestion = QuizQuestionHome.findByPrimaryKey( nQuestionId );
        if ( optQuestion.isEmpty( ) )
        {
            addError( "module.wiki.quiz.quiz.error.questionNotFound", getLocale( request ) );
            return redirect( request, URL_WIKI_HOME );
        }

        QuizQuestion question = optQuestion.get( );
        BeanUtil.populate( question, request );

        Quiz quiz = QuizService.findById( question.getIdQuiz( ) );
        if ( quiz == null )
        {
            addError( "module.wiki.quiz.quiz.error.notFound", getLocale( request ) );
            return redirect( request, URL_WIKI_HOME );
        }

        Optional<AbstractWikiItem> optBook = WikiItemHome.findByPrimaryKey( quiz.getIdBook( ) );
        if ( optBook.isEmpty( ) || !WikiAccessControlService.canEdit( user, optBook.get( ) ) )
        {
            addError( "module.wiki.quiz.quiz.error.accessDenied", getLocale( request ) );
            return redirectToQuizList( request, quiz.getIdBook( ) );
        }

        List<QuizAnswer> answers = extractAnswersFromRequest( request, question.getId( ) );
        if ( answers.size( ) < 2 )
        {
            addError( "module.wiki.quiz.quizQuestion.error.minAnswers", getLocale( request ) );
            Book book = (Book) optBook.get( );
            question.setAnswers( answers );

            _models.put( MARK_QUIZ, quiz );
            _models.put( MARK_QUESTION, question );
            _models.put( MARK_QUESTION_TYPES, QuestionType.values( ) );
            _models.put( MARK_IS_EDIT, true );
            populateBookSidebarModel( _models, user, book );
            _models.put( SecurityTokenService.MARK_TOKEN, _securityTokenService.getToken( request, ACTION_UPDATE_QUESTION ) );

            return getXPage( TEMPLATE_QUESTION_FORM, getLocale( request ) );
        }

        QuizService.updateQuestion( question );
        QuizService.replaceAnswers( question.getId( ), answers );

        String [ ] strQuestionPageIds = request.getParameterValues( PARAMETER_QUESTION_PAGES );
        List<Integer> listPageIds = new ArrayList<>( );
        if ( strQuestionPageIds != null )
        {
            for ( String strPageId : strQuestionPageIds )
            {
                try
                {
                    listPageIds.add( Integer.parseInt( strPageId ) );
                }
                catch( NumberFormatException e )
                {
                }
            }
        }
        QuizQuestionHome.setSourcePages( question.getId( ), listPageIds );

        addInfo( "module.wiki.quiz.message.questionUpdated", getLocale( request ) );

        Map<String, String> params = new HashMap<>( );
        params.put( PARAMETER_QUIZ_ID, String.valueOf( question.getIdQuiz( ) ) );
        return redirect( request, VIEW_MANAGE_QUESTIONS, params );
    }

    /**
     * Processes question deletion.
     *
     * @param request
     *            the HTTP servlet request
     * @return redirect to manage questions view
     * @throws UserNotSignedException
     *             if user is not authenticated
     */
    @Action( ACTION_DELETE_QUESTION )
    public XPage doDeleteQuestion( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = checkAuthentication( request );

        String strQuestionId = request.getParameter( PARAMETER_QUESTION_ID );
        int nQuestionId = Integer.parseInt( strQuestionId );

        Optional<QuizQuestion> optQuestion = QuizQuestionHome.findByPrimaryKey( nQuestionId );
        if ( optQuestion.isEmpty( ) )
        {
            addError( "module.wiki.quiz.quiz.error.questionNotFound", getLocale( request ) );
            return redirect( request, URL_WIKI_HOME );
        }

        QuizQuestion question = optQuestion.get( );
        Quiz quiz = QuizService.findById( question.getIdQuiz( ) );

        if ( quiz == null )
        {
            addError( "module.wiki.quiz.quiz.error.notFound", getLocale( request ) );
            return redirect( request, URL_WIKI_HOME );
        }

        Optional<AbstractWikiItem> optBook = WikiItemHome.findByPrimaryKey( quiz.getIdBook( ) );
        if ( optBook.isEmpty( ) || !WikiAccessControlService.canEdit( user, optBook.get( ) ) )
        {
            addError( "module.wiki.quiz.quiz.error.accessDenied", getLocale( request ) );
            return redirectToQuizList( request, quiz.getIdBook( ) );
        }

        int nQuizId = question.getIdQuiz( );
        QuizService.deleteQuestion( nQuestionId );
        addInfo( "module.wiki.quiz.message.questionRemoved", getLocale( request ) );

        Map<String, String> params = new HashMap<>( );
        params.put( PARAMETER_QUIZ_ID, String.valueOf( nQuizId ) );
        return redirect( request, VIEW_MANAGE_QUESTIONS, params );
    }

    /**
     * Processes question reordering.
     *
     * @param request
     *            the HTTP servlet request
     * @return redirect to manage questions view
     * @throws UserNotSignedException
     *             if user is not authenticated
     */
    @Action( ACTION_REORDER_QUESTIONS )
    public XPage doReorderQuestions( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = checkAuthentication( request );

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
            addError( "module.wiki.quiz.quiz.error.notFound", getLocale( request ) );
            return redirect( request, URL_WIKI_HOME );
        }

        Optional<AbstractWikiItem> optBook = WikiItemHome.findByPrimaryKey( quiz.getIdBook( ) );
        if ( optBook.isEmpty( ) || !WikiAccessControlService.canEdit( user, optBook.get( ) ) )
        {
            addError( "module.wiki.quiz.quiz.error.accessDenied", getLocale( request ) );
            return redirectToQuizList( request, quiz.getIdBook( ) );
        }

        String [ ] questionIds = request.getParameterValues( PARAMETER_QUESTION_ID );
        if ( questionIds != null )
        {
            List<Integer> listQuestionIds = new ArrayList<>( );
            for ( String strQuestionId : questionIds )
            {
                listQuestionIds.add( Integer.parseInt( strQuestionId ) );
            }
            QuizService.reorderQuestions( listQuestionIds );
        }

        addInfo( "module.wiki.quiz.message.questionsReordered", getLocale( request ) );

        Map<String, String> params = new HashMap<>( );
        params.put( PARAMETER_QUIZ_ID, strQuizId );
        return redirect( request, VIEW_MANAGE_QUESTIONS, params );
    }

    /**
     * Extracts answers from the HTTP request.
     *
     * @param request
     *            the HTTP servlet request
     * @param nQuestionId
     *            the question identifier
     * @return list of quiz answers
     */
    private List<QuizAnswer> extractAnswersFromRequest( HttpServletRequest request, int nQuestionId )
    {
        List<QuizAnswer> answers = new ArrayList<>( );

        String strAnswerCount = request.getParameter( PARAMETER_ANSWER_COUNT );
        if ( strAnswerCount == null || strAnswerCount.isEmpty( ) )
        {
            return answers;
        }

        int nAnswerCount = Math.min( Integer.parseInt( strAnswerCount ), MAX_ANSWER_COUNT );
        String strQuestionType = request.getParameter( PARAMETER_QUESTION_TYPE_CODE );
        boolean bIsSingleChoice = "TRUE_FALSE".equals( strQuestionType );

        int nCorrectAnswerIndex = -1;
        if ( bIsSingleChoice )
        {
            String strCorrectIndex = request.getParameter( PARAMETER_ANSWER_CORRECT_RADIO );
            if ( strCorrectIndex != null && !strCorrectIndex.isEmpty( ) )
            {
                try
                {
                    nCorrectAnswerIndex = Integer.parseInt( strCorrectIndex );
                }
                catch( NumberFormatException e )
                {
                }
            }
        }

        for ( int i = 0; i < nAnswerCount; i++ )
        {
            String strAnswerText = request.getParameter( PARAMETER_ANSWER_TEXT + i );
            if ( strAnswerText == null || strAnswerText.trim( ).isEmpty( ) )
            {
                continue;
            }

            QuizAnswer answer = new QuizAnswer( );
            answer.setIdQuestion( nQuestionId );
            answer.setAnswerText( strAnswerText.trim( ) );
            answer.setDisplayOrder( i );

            if ( bIsSingleChoice )
            {
                answer.setIsCorrect( i == nCorrectAnswerIndex );
            }
            else
            {
                String strIsCorrect = request.getParameter( PARAMETER_ANSWER_CORRECT + i );
                answer.setIsCorrect( strIsCorrect != null );
            }

            String strMatchTarget = request.getParameter( PARAMETER_ANSWER_MATCH + i );
            if ( strMatchTarget != null && !strMatchTarget.trim( ).isEmpty( ) )
            {
                answer.setMatchTarget( strMatchTarget.trim( ) );
            }

            String strCorrectOrder = request.getParameter( PARAMETER_ANSWER_ORDER + i );
            if ( strCorrectOrder != null && !strCorrectOrder.trim( ).isEmpty( ) )
            {
                try
                {
                    answer.setCorrectOrder( Integer.parseInt( strCorrectOrder.trim( ) ) );
                }
                catch( NumberFormatException e )
                {
                }
            }

            answers.add( answer );
        }

        return answers;
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

    /**
     * Redirects to the quiz list for a book.
     *
     * @param request
     *            the HTTP servlet request
     * @param nBookId
     *            the book identifier
     * @return redirect XPage
     */
    private XPage redirectToQuizList( HttpServletRequest request, int nBookId )
    {
        return redirect( request, URL_QUIZ_LIST + nBookId );
    }
}
