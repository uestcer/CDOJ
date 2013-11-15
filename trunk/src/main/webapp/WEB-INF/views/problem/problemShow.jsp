<%--
Problem statement
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ taglib prefix='fn' uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
	<head>
		<title><c:out value="${targetProblem.title}"/></title>
	</head>
	<body>

		<div id="problem-show">
			<div id="mzry1992-container">
				<div class="row">
					<div class="col-md-12" id="problem_title" value="<c:out value="${targetProblem.problemId}"/>">
						<h1>
							<c:out value="${targetProblem.title}"/>
							<c:if test="${targetProblem.isSpj == true}">
								<sup>
									<span class="label label-danger tags" style="margin: 0 0 0 8px; font-size: 16px;">SPJ</span>
								</sup>
							</c:if>
						</h1>
					</div>
					<%--
					<div class="pure-u-1-5">
						<c:if test="${currentUser.type == 1}">
							<a href="<c:url value="/admin/problem/editor/${targetProblem.problemId}"/>">
								<i class="icon-pencil"></i>
								Edit problem statement
							</a>
							<br/>
							<a href="<c:url value="/admin/problem/data/${targetProblem.problemId}"/>">
								<i class="icon-cog"></i>
								Edit problem data
							</a>
						</c:if>
					</div>
					--%>
					<div class="col-md-12">
						<div id="problem-show-tab" style="margin: 16px 0;">
							<ul class="nav nav-pills">
								<li class="active"><a href="#tab-problem-show" data-toggle="tab">Problem</a></li>
								<li><a href="#tab-problem-submit" data-toggle="tab">Submit</a></li>
								<li class="disabled"><a href="#">Status</a></li>
								<li class="disabled"><a href="#">Discus</a></li>
							</ul>
						</div>
					</div>

					<div class="row tab-content">
						<div class="tab-pane active" id="tab-problem-show">
							<div class="col-md-12">
								<dl class="dl-horizontal">
									<dt>Time limit</dt>
									<dd><span>${targetProblem.javaTimeLimit} / ${targetProblem.timeLimit} ms (Java / others)</span>
									</dd>
									<dt>Memory limit</dt>
									<dd>
										<span>${targetProblem.javaMemoryLimit} / ${targetProblem.memoryLimit} kb (Java / others)</span>
									</dd>
									<dt>Total accepted</dt>
									<dd><span>${targetProblem.solved}</span></dd>
									<dt>Total submissions</dt>
									<dd><span>${targetProblem.tried}</span></dd>
								</dl>
							</div>

							<div class="col-md-12" id="problem_description" type="markdown">
								<textarea>${targetProblem.description}</textarea>
							</div>

							<div class="col-md-12">
								<h2>Input</h2>
								<div id="problem_input" type="markdown">
									<textarea>${targetProblem.input}</textarea>
								</div>
							</div>

							<div class="col-md-12">
								<h2>Output</h2>
								<div id="problem_output" type="markdown">
									<textarea>${targetProblem.output}</textarea>
								</div>
							</div>

							<div class="col-md-12">
								<h2>Sample input and output</h2>
								<table class="table">
									<thead>
										<tr>
											<th>Sample Input</th>
											<th>Sample Output</th>
										</tr>
									</thead>
									<tbody class="font-code">
										<tr>
											<td>
												<div class="sample" type="no-prettify">
													${fn:replace(targetProblem.sampleInput, brToken, '<br/>')}
												</div>
											</td>
											<td>
												<div class="sample" type="no-prettify">
													${fn:replace(targetProblem.sampleOutput, brToken, '<br/>')}
												</div>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
							<c:if test="${targetProblem.hint != ''}">
								<div class="col-md-12">
									<h2>Hint</h2>

									<div class="" id="problem_hint" type="markdown">
										<textarea>${targetProblem.hint}</textarea>
									</div>
								</div>
							</c:if>
							<c:if test="${targetProblem.source != ''}">
								<div class="col-md-12">
									<h2>Source</h2>

									<div class="" id="problem_source">
										${targetProblem.source}
									</div>
								</div>
							</c:if>
						</div>

						<div class="tab-pane" id="tab-problem-submit">
							<div class="col-md-12">
								<div class="panel panel-default">
								  <div class="panel-body">
										<form>
											<textarea class="cdoj-submit-area" id="code-content"></textarea>
										</form>
								  </div>
									<div class="panel-footer">
										<div class="btn-group" data-toggle="buttons">
											<c:forEach var="language" items="${languageList}">
												<label class="btn btn-default">
													<input type="radio" name="language"
															value="<c:out value="${language.languageId}"/>"> 
													<c:out value="${language.name}"/>
												</label>
											</c:forEach>
										</div>
										<button type="button" class="pull-right btn btn-danger" id="submit-code" data-loading-text="Submitting...">
											Submit
										</button>
									</div>
								</div>
							</div>
						</div>

						<div class="tab-pane" id="tab-problem-status">
						</div>

						<div class="tab-pane" id="tab-problem-discus">
						</div>
					</div>
				</div>
			</div>
		</div>
		<%--<page:applyDecorator name="body" page="/WEB-INF/views/status/statusModal.jsp"/>--%>

	</body>
</html>

