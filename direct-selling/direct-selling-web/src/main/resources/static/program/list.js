layui
		.use(
				[ 'table', 'jquery', 'layer', 'form', 'laytpl', 'laydate' ],
				function() {
					// 获取jquery 并声明为$
					var $ = layui.jquery, form = layui.form, table = layui.table, laydate = layui.laydate;

					var sceneId = GetQueryString("sceneId");
					var sceneDto = new Object;
					$.ajax({
						url: '/program/queryScene?sceneId='+sceneId,
			            async: false,
			            type:"POST",
			            dataType: 'json',
			            contentType:"application/json;charset=UTF-8",
			            success: function(data){
			            	sceneDto = data.result;
			            }
					});

					// 参数
					var programInfo = {
						id : "program-list-page",
						url : "/program/searchMaterialList",
						page: true,
						where : { // 设定异步数据接口的额外参数，任意设
							sceneId : sceneId
						},
						cols : [ [
                            	{
                                    templet: "#checkbd",
                                    title: "<input type='checkbox' name='checkbox_all' lay-skin='primary' lay-filter='checkbox_all'>",
                                    width: 60,
                                    templet : function(d) {
                                        return '<input type="checkbox" data-id="'+d.id+'" name="checkbox_id" lay-skin="primary" lay-filter="checkbox_id"> ';
                                    }
                            	},
								{
									field : 'id',
									title : '节目ID',
									minWidth: 5
								},
								{
									field : 'beginTime',
									title : '时间',
									minWidth: 250,
									templet : function(d) {
										return programTimes(d);
									}
								},
								{
									field : 'programTitle',
									title : '子标题',
									minWidth: 100
								},
								{
                                	field : 'programType',
                                	title : '节目类型',
                                	minWidth: 100,
                                	templet : function(d) {
                                    	var programName = "";
                                    	if(d.programType != null){
                                        	if (d.programType == 1){
                                            	programName = "预热";
                                        	}else if (d.programType == 2) {
                                            	programName = "正式";
                                        	}
                                    	}
                                    	return programName;
                                	}
                            	},
								{
									field : 'LiveSceneDealerBrands',
									title : '品牌&经销商',
									minWidth: 200,
									templet : function(d) {
										var liveSceneDealerBrands = "";
										$.each(d.liveSceneDealerBrands,function(index,value){
											liveSceneDealerBrands += value.brandName+"&nbsp;&nbsp;"+value.dealerName+"<br>";
										});
										var str = programStatus(d);
										if(str == '未开始'){
											var programTime = programTimes(d);
											liveSceneDealerBrands+='<button type="button" class="layui-btn layui-btn-xs layui-btn-normal" programTitle = "'+d.programTitle+'" programTime="'+programTime+'" id="programDealerList_'+d.id+'" name="programDealerList" value="'+d.id+'"><i class="layui-icon layui-icon-set-sm"></i>配置</button>';
										}else {
                                            liveSceneDealerBrands+='<button  style="display: none" type="button" class="layui-btn layui-btn-xs layui-btn-normal" programTitle = "'+d.programTitle+'" programTime="'+programTime+'" id="programDealerList_'+d.id+'" name="programDealerList" value="'+d.id+'"><i class="layui-icon layui-icon-set-sm"></i>配置</button>';
                                        }
										
										return liveSceneDealerBrands;
									}
								},
								{
									field : 'liveAddress',
									title : '直播链接',
									minWidth: 100,
									templet : function(d) {
										var liveAddress = d.liveAddress;
										if(liveAddress == null){
											liveAddress = "";
										}
										var str = programStatus(d);
										if(str == '未开始'){
											var programTime = programTimes(d);
											var button = '<button type="button"  class="layui-btn layui-btn-xs layui-btn-normal" programTime="'+programTime+'" name="liveAddressList" id ="'+d.id+'" value="'+liveAddress+'"><i class="layui-icon layui-icon-set-sm"></i>配置</button>';
											if(liveAddress != ""){
												button="<br>"+button;
											}
											liveAddress+=button
										}else {
                                            var programTime = programTimes(d);
                                            var button = '<button style="display: none" type="button"  class="layui-btn layui-btn-xs layui-btn-normal" programTime="'+programTime+'" name="liveAddressList" id ="'+d.id+'" value="'+liveAddress+'"><i class="layui-icon layui-icon-set-sm"></i>配置</button>';
                                            if(liveAddress != ""){
                                                button="<br>"+button;
                                            }
                                            liveAddress+=button
										}
										return liveAddress;
									}
								},
								{
									field : 'liveQrcodePic',
									title : '直播二维码',
									minWidth: 100,
									templet : function(d) {
										var button = "";
										if(d.liveAddress != '' && d.liveAddress != null){
											button = '<button type="button"  class="layui-btn layui-btn-xs layui-btn-normal" liveQrcodePic="'+d.id+'" name="liveQrcodePicList" value="'+d.liveAddress+'" ><i class="layui-icon layui-icon-share"></i>生成</button>'
										}
										return button;
									}
								},
								{
									field : 'b',
									title : '节目海报素材（经销商）',
									minWidth: 100,
									templet : function(d) {
										return '<button type="button" id="'+d.id+'" class="layui-btn layui-btn-xs layui-btn-normal" name="dealerPicList"><i class="layui-icon layui-icon-share"></i>生成</button>';
									}
								},
								{
									field : 'status',
									title : '状态',
									minWidth: 80,
									templet : function(d) {
										var title = programStatus(d);
										var style="layui-bg-red";
										if(title == '进行中'){
											style="layui-bg-green";
										}else if(title == '未开始'){
											style="layui-bg-orange";
										}
										return '<span class="layui-badge '+style+'">'+title+'</span>';
									}
								}, {
									title : '操作',
									minWidth: 150,
									toolbar : "#programbar"
								} ] ]
					};
					// 初始渲染
					var programListTab = render(layui, '#program-list-page',
							programInfo);

					// 监听工具条
					table.on('tool(programbar)', function(obj) {
						var data = obj.data;
						if (obj.event === 'edit') {
							showDetailView(data);
						} else if (obj.event === "del") {
							updateStatus(data, "确定删除该条节目？");
						}
					});
					// 配置品牌经销商
					var programDealerList="";
					$(document).on("click", "button[name='programDealerList']", function(ele) {
						var id = $(this).val()
						var programTime = $(this).attr("programTime");
						var times = programTime.split("~");
						var timeData = new Object();
						timeData.beginTime = times[0];
						timeData.endTime = times[1];
						var str = programStatus(timeData)
						if(str != '未开始'){
							alterError("该节目"+str+"，请重新选择！");
							reload();
							return false;
						}
						var programTitle = $(this).attr("programTitle");
						var html = document.getElementById("programDealertmp").innerHTML;
							programDealerList = layer.open({
							title : "品牌&经销商",
							type : 1,
							area : [ '80%', '80%' ],
							content : html,
							success : function(layero, index) {
								$("#programTime").text("时间："+programTime);
								$("#programTitle").text("节目名称："+programTitle);
								$("input[name='id']").val(id);
								$("input[name='sceneId']").val(sceneId);
								// 查询已选所有品牌
								var brandsCheck = "";
								$.ajax({
									url: '/program/searchBrands?id='+id+'&sceneId='+sceneId,
						            async: false,
						            type:"POST",
						            dataType: 'json',
						            contentType:"application/json;charset=UTF-8",
						            success: function(data){
						            	brandsCheck = data.data;
						            }
								});
								
								// 所有品牌
								var brandIds = "";
								$.ajax({
									url: '/program/searchBrands?sceneId='+sceneId,
						            async: false,
						            type:"POST",
						            dataType: 'json',
						            contentType:"application/json;charset=UTF-8",
						            success: function(data){
						            	brandIds = data.data;
						            	if(brandsCheck != null){
						            		var trs="";
						            		$.each(brandsCheck,function(index,brandCheck){
						            			var select="";
						            			var dealerIdTd="";
						            			$.each(brandIds,function(indexBrand,brand){
						            				var sel = "";
						            	    		if(brandCheck.brandId == brand.brandId){
						            	    			sel = "selected = 'true'";
						            	    			var dealersCheck="";
						            	    			$.ajax({
															url: '/program/searchDealers?id='+id+'&sceneId='+sceneId+'&brandId='+brand.brandId,
												            async: false,
												            type:"POST",
												            dataType: 'json',
												            contentType:"application/json;charset=UTF-8",
												            success: function(data){
												            	dealersCheck=data.data;
												            	var dealers="";
												            	$.ajax({
																	url: '/program/searchDealers?sceneId='+sceneId+'&brandId='+brand.brandId,
														            async: false,
														            type:"POST",
														            dataType: 'json',
														            contentType:"application/json;charset=UTF-8",
														            success: function(data){
														            	dealers = data.data;
														            }
																});
												            	$.each(dealers,function(indexDealer,dealer){
												            		var checked="";
												            		$.each(dealersCheck,function(indexDealer,dealerCheck){
												            			if(dealerCheck.dealerId == dealer.dealerId){
													            			checked = "checked = 'true'";
											            	    		}
												            		});
												            		dealerIdTd+='<input '+checked+' type="checkbox" name="'+dealer.dealerId+'" title="'+dealer.dealerName+'" lay-skin="primary" >&nbsp;';
												            	});
												            }
														});
						            	    		}
						            	    		
						            	    		select +="<option "+sel+" value='" + (!brand.brandId ? "" : brand.brandId)+ "'>" + brand.brandName + "</option>";
						            			});
						            			trs += '<tr>'+
						            			'<td><select lay-search="" lay-filter="select-brand">'+select+'</select></td>' +
						            			'<td>'+dealerIdTd+'</td>' +
						            			'<td><button name="button-del" type="button"  class="layui-btn layui-btn-xs">删除</button></td>' +
						            			'</tr>';
						            			
						            		});
						            		$("#castTr").before(trs);
						            		layui.form.render();
						            		$("button[name='button-del']").click(function(){
												 $(this).parent().parent().remove();
											 });
						            	}
						            }
								});
								
								layui.form.on('select(select-brand)', function (data) {
									var dealerIdTd="";
									if(data.value != ""){
										$.ajax({
											url: '/program/searchDealers?sceneId='+sceneId+'&brandId='+data.value,
											async: false,
											type:"POST",
											dataType: 'json',
											contentType:"application/json;charset=UTF-8",
											success: function(data){
												$.each(data.data,function(indexDealer,dealer){
													dealerIdTd+='<input type="checkbox" name="'+dealer.dealerId+'" title="'+dealer.dealerName+'" lay-skin="primary" >&nbsp;';
												});
											}
										});
									}
									var obj = $(this).parent().parent().parent().next();
									obj.empty();
									obj.html(dealerIdTd);
									layui.form.render();
							   });
								// 添加
								 $("#addProgramDealer").click(function(){
									if(brandIds != null){

                                        var numb = $("#dealerBrands").find("tr").length;
										if (numb>2){
											alterError("参展品牌只能设置一个");
											return false;
										}

										var select = '<option value="">请选择品牌</option>';
										$.each(brandIds,function(indexBrand,brand){
											var cast = "<option value='" + brand.brandId + "'>" + brand.brandName + "</option>";
											select += cast;
										});
										var tr = '<tr>' +
										'<td><select lay-search="" lay-filter="select-brand">'+select+'</select></td>' +
										'<td></td>' +
										'<td><button type="button"  class="layui-btn layui-btn-xs" name="button-del">删除</button></td>' +
										'</tr>';
										$("#castTr").before(tr);
										layui.form.render();
										 $("button[name='button-del']").click(function(){
											 $(this).parent().parent().remove();
										 });
										layui.form.on('select(select-brand)', function (data) {
											var dealerIdTd="";
											if(data.value !=""){
												$.ajax({
													url: '/program/searchDealers?sceneId='+sceneId+'&brandId='+data.value,
													async: false,
													type:"POST",
													dataType: 'json',
													contentType:"application/json;charset=UTF-8",
													success: function(data){
														$.each(data.data,function(indexDealer,dealer){
															dealerIdTd+='<input type="checkbox" name="'+dealer.dealerId+'" title="'+dealer.dealerName+'" lay-skin="primary" >&nbsp;';
														});
													}
												});
											}
											$(this).parent().parent().parent().next().html(dealerIdTd);
											layui.form.render();
										});
									}
								});
								
							}
						});
				});
					form.on('submit(updateProgramDealer)', function(data){
						$("input[name='sceneId']").val(sceneId);
						var strs = [];
						var flag = false;
						var allFlag = false;
						var againFlag = false;
						var brandIds = "";
						$("#dealerBrands").find("tr").each(function(index,element){
							if(index != 0 && $(element).attr("id") != "castTr"){
								allFlag = true;
								var brandId="";
								$(element).find("td").each(function(ind,ele){
									if(ind == 0){
										flag = false;
										$(ele).find("select").each(function(indOp,option){
											if($(this).val() !=''){
												flag = true;
												brandId=$(this).val();
												if(brandIds.indexOf(brandId+",")>=0){
													againFlag = true;
													return false;
												}
												brandIds+=brandId+",";
											}
										});
//										if(!flag){
//											return false;
//										}
										if(againFlag){
											return false;
										}
									}
									if(ind == 1){
										flag = false;
										var dealerIds = "";
										$(ele).find("div").each(function(indDiv,div){
											if($(div).attr("class").indexOf("layui-form-checked") >=0){
												flag = true;
												var str = new Object();
												str.brandId = brandId;
												str.dealerId = $(div).prev().attr("name");
												strs.push(str);
											}
										});
//										if(!flag){
//											return false;
//										}
									}
								});
								
							}
						  });
//						if(!flag || !allFlag){
//							alterError("请选择品牌以及对应经销商！");
//							return false;
//						}
						if(againFlag){
							alterError("品牌重复，请重新选择！");
							return false;
						}
						data.field.programDealerBrands = strs;
						$.ajax({
							url: '/program/updateProgramDealer',
				            async: false,
				            type:"POST",
				            dataType: 'json',
				            data:JSON.stringify(data.field),
				            contentType:"application/json;charset=UTF-8",
				            success: function(data){
				            	if(data.code == 200){
				            		alterSuccess('成功');
				            		layer.close(programDealerList);
				            		reload();
				            	}else{
				            		alterError(data.msg);
				            	}
				            }
						});
					    return false;
					 });
					// 配置直播链接
					var liveAddressListIndex="";
					$(document).on("click", "button[name='liveAddressList']", function(ele) {
						var programTime = $(this).attr("programTime");
						var times = programTime.split("~");
						var timeData = new Object();
						timeData.beginTime = times[0];
						timeData.endTime = times[1];
						var str = programStatus(timeData)
						if(str != '未开始'){
							alterError("该节目"+str+"，请重新选择！");
							reload();
							return false;
						}
						
						var value = $(this).val();
						var id = $(this).attr("id");
						var html = document.getElementById("liveAddresstmp").innerHTML;
						liveAddressListIndex = layer.open({
							title : "直播链接",
							type : 1,
							area : [ '50%', '30%' ],
							content : html,
							success : function(layero, index) {
								$("input[name='id']").val(id);
								$("input[name='liveAddress']").val(value);
								$("input[name='sceneId']").val(sceneId);
							}
						});
					});
					form.on('submit(addLiveAddress)', function(data){
						$("input[name='sceneId']").val(sceneId);
						$.ajax({
							url: '/program/update',
				            async: false,
				            type:"POST",
				            dataType: 'json',
				            data:JSON.stringify(data.field),
				            contentType:"application/json;charset=UTF-8",
				            success: function(data){
				            	if(data.code == 200){
				            		alterSuccess('成功');
				            		layer.close(liveAddressListIndex);
				            		reload();
				            	}else{
				            		alterError(data.msg);
				            	}
				            }
						});
					    return false;
					 });
					
					$(document).on("click", "button[name='liveQrcodePicList']", function(ele) {
						var value = $(this).val();
						var id = $(this).attr("liveQrcodePic");
						$(this).parent().html('<div id="qrcode_'+id+'" style="width:100px; height:100px; margin-top:15px;"></div>');
						var qrcode = new QRCode(document.getElementById("qrcode_"+id), {
							width : 100,
							height : 100
						});
						qrcode.makeCode(value);
					});
					$(document).on("click", "button[name='dealerPicList']", function(ele) {
						// layer.msg("敬请期待！",{icon : 6,time : 2000});

                        var id = $(this).attr("id");
                        var programDealerListHtml = $("#programDealerList_"+id).parent("div").parent("td").html();
                        var liveAddressHtml = $("#"+id).parent("div").parent("td").html();
                        if (programDealerListHtml.indexOf("<br>") < 0) {
                            alterError("请先配置经销商品牌");
                            return false;
                        }
                        if (liveAddressHtml.indexOf("<br>") < 0){
                            alterError("请先配置直播链接");
                            return false;
						}
                        var programDealerListHtml = $("#programDealerList_"+id).parent("div").parent("td").html();

                        var programDealers = programDealerListHtml.split("<br>");
                        var time_numb = (programDealers.length-1)*5000;
                        if (time_numb == NaN){
                            alterLoading("生成中",5000);
						} else {
                            alterLoading("生成中",time_numb);
                        }
                        window.open("/program/poster?programId=" + id, "_self");
					});
					
					// 添加节目
					var programAddIndex="";
					$("#program-add").click(function() {
						if(sceneDto!=""){
							var str = programStatus(sceneDto);
							if(str == "已结束"){
								alterError('场次活动已结束，不允许添加节目！');
								return false;
							}else if(str == "进行中"){
								if(sceneDto.upState == 0){
									alterError('场次活动已下架，不允许添加节目！');
									return false;
								}
							}
						}
						
						var html = document.getElementById("addtmp").innerHTML;
						    programAddIndex = layer.open({
							title : "添加节目",
							type : 1,
							area : [ '80%', '40%' ],
							content : html,
							success : function(layero, index) {
								$("input[name='sceneId']").val(sceneId);
								laydate.render({
							        elem: '#beginTime'
							        ,type: 'datetime'
							        ,trigger: 'click'
							      });
								laydate.render({
							        elem: '#endTime'
							        ,type: 'datetime'
							        ,trigger: 'click'
							      });
                                loadProgramTime();
                                layui.form.render();
							}
						});
					});
					form.on('submit(addProgram)', function(data){
						 var url = '/program/save';
						 var index =programAddIndex;
						 if(data.field.id != ''){
							 url = '/program/update';
							 index =programEditIndex;
						 }else{
							 if(sceneDto!=""){
								 var str = programStatus(sceneDto);
								 if(str == "已结束"){
									 alterError('本次活动已结束，不允许添加节目！');
									 return false;
								 }else if(str == "进行中"){
										if(sceneDto.upState == 0){
											alterError('本次活动已下架，不允许添加节目！');
											return false;
										}
									}
							 }
						 }
						 
						 var beginTime = data.field.beginTime;
						 var endTime = data.field.endTime;
						 if(new Date(beginTime).getTime() < new Date().getTime()){
							 alterError('开始时间不能小于当前时间');
					          return false;
					     }
						 if(new Date(endTime).getTime() < new Date(beginTime).getTime()){
							  alterError('结束时间不能小于开始时间');
					          return false;
					     }
						 
						 if(sceneDto != null){
						 	var beginT="";
						 	var endT="";
						 	var error1="";
						 	var error2="";
						 	//1:预热直播  2:正式直播
						 	if (data.field.programType==1){
                                beginT=sceneDto.readyBeginTime;
                                endT=sceneDto.readyEndTime;
                                error1="开始时间不能小于活动预热开始时间";
                                error2="结束时间不能大于活动预热结束时间";
							}else if (data.field.programType==2) {
                                beginT=sceneDto.formalBeginTime;
                                endT=sceneDto.formalEndTime;
                                error1="开始时间不能小于活动正式开始时间";
                                error2="结束时间不能大于活动正式结束时间";
							}
							 if(new Date(beginTime).getTime() < new Date(beginT).getTime()){
								 alterError(error1);
						          return false;
						     }

							 if(new Date(endTime).getTime() > new Date(endT).getTime()){
								  alterError(error2);
						          return false;
						     }
						 }

						 data.field.beginTime = new Date(beginTime);
						 data.field.endTime = new Date(endTime);
						$.ajax({
							url: url,
				            async: false,
				            type:"POST",
				            dataType: 'json',
				            data:JSON.stringify(data.field),
				            contentType:"application/json;charset=UTF-8",
				            success: function(data){
				            	if(data.code == 200){
				            		alterSuccess('成功');
				            		layer.close(index);
				            		reload();
				            	}else{
				            		alterError(data.msg);
				            	}
				            }
						});
					    return false;
					 });
                    $("#posters-create").click(function () {
                        var ids = [];
                        var bo=false; //经销商品牌
                        var bo1=false; //直播链接
                        var time_count =0;
                        $.each($("input[name='checkbox_id']:checked"), function (i, value) {
                        	var id=$(this).attr("data-id");
                            var programDealerListHtml = $("#programDealerList_"+id).parent("div").parent("td").html();
                            if (programDealerListHtml.indexOf("<br>") < 0) {
                                bo = true;
                            }
                            var liveAddressHtml = $("#"+id).parent("div").parent("td").html();
                            if (liveAddressHtml.indexOf("<br>")<0){
                                bo1 = true;
							}
                            var programDealers = programDealerListHtml.split("<br>");
                            time_count = time_count + programDealers.length - 1;
                            ids[i] = id;
                        });

                        if (bo){
                            alterError("请先配置经销商品牌");
                            return false;
						}
                        if (bo1){
                            alterError("请先配置直播链接");
                            return false;
                        }
                        if (ids.length==0){
                            alterError("请选择要生成海报的节目！");
                            return false;
						}
						if (ids.length>5){
                            alterError("单次批量不超过5个！");
                            return false;
						}
						if (ids.length>0){
                            var time_numb = time_count * 6000;
                            if (time_numb == NaN){
                                alterLoading("生成中", 10000);
                            } else {
                                alterLoading("生成中",time_numb);
                            }
                            $.each($("input[name='checkbox_id']"), function (i, value) {
                                $(this).prop("checked", false);
                            });
                            $("input[name='checkbox_all']").prop("checked", false);
                            form.render();
                            window.open("/program/poster/zip?programIds=" + ids, "_self");
						}
                    });
                    form.on("checkbox(checkbox_all)", function () {
                        var status = $(this).prop("checked");
                        $.each($("input[name='checkbox_id']"), function (i, value) {
                            $(this).prop("checked", status);
                        });
                        form.render();
                    });
					function updateStatus(data, msg) {
						var str = programStatus(data)
						if(str != '未开始'){
							alterError("该节目"+str+"，请重新选择！");
							reload();
							return false;
						}
						layer.msg(msg,
								{
									icon : 5,
									time : 0,
									btn : [ '确定', '取消' ],
									yes : function(index) {
										var str=new Object();
										str.id=data.id;
										str.deleteState=1;
										$.ajax({
											url: '/program/update',
								            async: false,
								            type:"POST",
								            dataType: 'json',
								            data:JSON.stringify(str),
								            contentType:"application/json;charset=UTF-8",
								            success: function(data){
								            	if(data.code == 200){
								            		alterSuccess('成功');
								            		layer.close(index);
								            		reload();
								            	}else{
								            		alterError(data.msg);
								            	}
								            }
										});
									}
								});
					}
					var programEditIndex="";
					function showDetailView(data) {
						var str = programStatus(data)
						if(str != '未开始'){
							alterError("该节目"+str+"，请重新选择！");
							reload();
							return false;
						}
						var html = document.getElementById("addtmp").innerHTML;
							programEditIndex = layer.open({
							title : "编辑节目",
							type : 1,
							area : [ '80%', '60%' ],
							content : html,
							success : function(layero, index) {
								$("input[name='id']").val(data.id);
								$("input[name='sceneId']").val(sceneId);
								laydate.render({
							        elem: '#beginTime'
							        ,type: 'datetime'
							        ,trigger: 'click'
							      });
								laydate.render({
							        elem: '#endTime'
							        ,type: 'datetime'
							        ,trigger: 'click'
							      });
								$("#beginTime").val((new Date(data.beginTime)).Format("yyyy-MM-dd HH:mm:ss"));
								$("#endTime").val((new Date(data.endTime)).Format("yyyy-MM-dd HH:mm:ss"));
								$("input[name='programTitle']").val(data.programTitle);
                                $("input[name='programType'][value='1']").attr("checked", data.programType == 1 ? true : false);
                                $("input[name='programType'][value='2']").attr("checked", data.programType == 2 ? true : false);
                                $("input[name='programType'][value='1']").attr("disabled","disabled");;
                                $("input[name='programType'][value='2']").attr("disabled","disabled");;
                                layui.form.render();
							}
						});
					}
					form.on('radio', function(data) {
                        loadProgramTime();
                    });

					function programTimes(d) {
						var time = (new Date(d.beginTime))
						.Format("yyyy-MM-dd HH:mm")
						+ "~"
						+ (new Date(d.endTime))
								.Format("yyyy-MM-dd HH:mm");
						return time;
					}
					function programStatus(d) {
						return (new Date().getTime() - new Date(
								d.beginTime).getTime()) >= 0 ? (new Date()
								.getTime() - new Date(d.endTime)
								.getTime()) <= 0 ? "进行中"
								: "已结束"
								: "未开始";
					}

					function reload() {
						programListTab.reload({
							where : { 
								sceneId : sceneId
							},
							page: {
				                curr: 1 
				            }
						});
					}

					function loadProgramTime(type) {
                        if(sceneDto != null){
                            var beginT="";
                            var endT="";
                            //1:预热直播  2:正式直播
                            var programType = $("input[name='programType']:checked").val();
                            if (programType==1){
                                beginT=sceneDto.readyBeginTime;
                                endT=sceneDto.readyEndTime;
                            }else if (programType==2) {
                                beginT=sceneDto.formalBeginTime;
                                endT=sceneDto.formalEndTime;
                            }
                            $("#beginTime").val((new Date(beginT)).Format("yyyy-MM-dd HH:mm:ss"));
                            $("#endTime").val((new Date(endT)).Format("yyyy-MM-dd HH:mm:ss"));
                        }
                    }
				});
function alterError(msg){
	layer.msg(msg,{icon : 2,time : 2000});
}
function alterSuccess(msg){
	layer.msg(msg,{icon : 1,time : 2000});
}
function alterLoading(msg,time_numb){
    layer.msg(msg,{icon : 16,time : time_numb});
}
function GetQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);// search,查询？后面的参数，并匹配正则
	if (r != null)
		return unescape(r[2]);
	return null;
}
